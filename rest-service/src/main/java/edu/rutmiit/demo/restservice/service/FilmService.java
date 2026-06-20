package edu.rutmiit.demo.restservice.service;

import edu.rutmiit.demo.filmsapicontract.dto.*;
import edu.rutmiit.demo.filmsapicontract.exception.ImdbIdAlreadyExistsException;
import edu.rutmiit.demo.filmsapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.restservice.event.FilmEventPublisher;
import edu.rutmiit.demo.restservice.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FilmService {

    private final InMemoryStorage storage;
    private final DirectorService directorService;
    private final FilmEventPublisher eventPublisher;

    public FilmService(InMemoryStorage storage,
                       @Lazy DirectorService directorService,
                       FilmEventPublisher eventPublisher) {
        this.storage = storage;
        this.directorService = directorService;
        this.eventPublisher = eventPublisher;
    }

    public FilmResponse findFilmById(Long id) {
        return Optional.ofNullable(storage.films.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Film", id));
    }

    public PagedResponse<FilmResponse> findAllFilms(Long directorId, String genre, Integer publishedYear,
                                                    String titleSearch, int page, int size) {
        Stream<FilmResponse> stream = storage.films.values().stream()
                .sorted((b1, b2) -> b1.getId().compareTo(b2.getId()));

        if (directorId != null) {
            stream = stream.filter(b -> b.getDirector() != null && b.getDirector().getId().equals(directorId));
        }
        if (genre != null && !genre.isBlank()) {
            stream = stream.filter(b -> genre.equalsIgnoreCase(b.getGenre()));
        }
        if (publishedYear != null) {
            stream = stream.filter(b -> publishedYear.equals(b.getPublishedYear()));
        }
        if (titleSearch != null && !titleSearch.isBlank()) {
            String q = titleSearch.toLowerCase();
            stream = stream.filter(b -> b.getTitle() != null && b.getTitle().toLowerCase().contains(q));
        }

        List<FilmResponse> allFilms = stream.toList();
        int totalElements = allFilms.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<FilmResponse> content = (from >= totalElements) ? List.of() : allFilms.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public FilmResponse createFilm(FilmRequest request) {
        validateImdbId(request.imdbId(), null);
        DirectorResponse director = directorService.findById(request.directorId());

        long id = storage.filmSequence.incrementAndGet();
        FilmResponse film = FilmResponse.builder()
                .id(id)
                .title(request.title())
                .imdbId(request.imdbId())
                .director(director)
                .description(request.description())
                .genre(request.genre())
                .publishedYear(request.publishedYear())
                .language(request.language())
                .createdAt(LocalDateTime.now())
                .build();
        storage.films.put(id, film);

        eventPublisher.publishCreated(film);

        return film;
    }

    public FilmResponse updateFilm(Long id, UpdateFilmRequest request) {
        FilmResponse existing = findFilmById(id);
        validateImdbId(request.imdbId(), id);

        FilmResponse updated = FilmResponse.builder()
                .id(id)
                .title(request.title())
                .imdbId(request.imdbId())
                .director(existing.getDirector())
                .description(request.description())
                .genre(request.genre())
                .publishedYear(request.publishedYear())
                .language(request.language())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        storage.films.put(id, updated);
        eventPublisher.publishUpdated(updated);
        return updated;
    }

    public FilmResponse patchFilm(Long id, PatchFilmRequest request) {
        FilmResponse existing = findFilmById(id);

        if (request.imdbId() != null && !request.imdbId().equalsIgnoreCase(existing.getImdbId())) {
            validateImdbId(request.imdbId(), id);
        }

        FilmResponse updated = FilmResponse.builder()
                .id(id)
                .title(request.title() != null ? request.title() : existing.getTitle())
                .imdbId(request.imdbId() != null ? request.imdbId() : existing.getImdbId())
                .director(existing.getDirector())
                .description(request.description() != null ? request.description() : existing.getDescription())
                .genre(request.genre() != null ? request.genre() : existing.getGenre())
                .publishedYear(request.publishedYear() != null ? request.publishedYear() : existing.getPublishedYear())
                .language(request.language() != null ? request.language() : existing.getLanguage())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        storage.films.put(id, updated);
        eventPublisher.publishUpdated(updated);
        return updated;
    }

    public void deleteFilm(Long id) {
        FilmResponse film = findFilmById(id);
        storage.films.remove(id);
        eventPublisher.publishDeleted(id, film.getTitle());
    }

    public void deleteFilmsByDirectorId(Long directorId) {
        List<Long> toDelete = storage.films.values().stream()
                .filter(b -> b.getDirector() != null && b.getDirector().getId().equals(directorId))
                .map(FilmResponse::getId)
                .toList();
        toDelete.forEach(storage.films::remove);
    }

    private void validateImdbId(String imdbId, Long currentFilmId) {
        storage.films.values().stream()
                .filter(b -> b.getImdbId().equalsIgnoreCase(imdbId))
                .filter(b -> !b.getId().equals(currentFilmId))
                .findAny()
                .ifPresent(b -> { throw new ImdbIdAlreadyExistsException(imdbId); });
    }
}
