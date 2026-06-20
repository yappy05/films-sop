package edu.rutmiit.demo.restservice.service;

import edu.rutmiit.demo.filmsapicontract.dto.*;
import edu.rutmiit.demo.filmsapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.restservice.event.DirectorEventPublisher;
import edu.rutmiit.demo.restservice.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DirectorService {
    private final InMemoryStorage storage;
    private final FilmService filmService;
    private final DirectorEventPublisher eventPublisher;

    public DirectorService(InMemoryStorage storage,
                         @Lazy FilmService filmService,
                         DirectorEventPublisher eventPublisher) {
        this.storage = storage;
        this.filmService = filmService;
        this.eventPublisher = eventPublisher;
    }

    public PagedResponse<DirectorResponse> findAll(int page, int size) {
        List<DirectorResponse> all = storage.directors.values().stream()
                .sorted(Comparator.comparingLong(DirectorResponse::getId))
                .toList();
        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<DirectorResponse> content = (from >= totalElements) ? List.of() : all.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public DirectorResponse findById(Long id) {
        return Optional.ofNullable(storage.directors.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Director", id));
    }

    public DirectorResponse create(DirectorRequest request) {
        long id = storage.directorSequence.incrementAndGet();
        String fullName = request.firstName() + " " + request.lastName();
        DirectorResponse director = DirectorResponse.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .fullName(fullName)
                .email(request.email())
                .bio(request.bio())
                .birthDate(request.birthDate())
                .nationality(request.nationality())
                .filmsCount(0)
                .build();
        storage.directors.put(id, director);
        eventPublisher.publishCreated(director);
        return director;
    }

    public DirectorResponse update(Long id, DirectorRequest request) {
        DirectorResponse existing = findById(id);
        String fullName = request.firstName() + " " + request.lastName();
        DirectorResponse updatedDirector = DirectorResponse.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .fullName(fullName)
                .email(request.email())
                .bio(request.bio())
                .birthDate(request.birthDate())
                .nationality(request.nationality())
                .filmsCount(existing.getFilmsCount())
                .build();
        storage.directors.put(id, updatedDirector);
        return updatedDirector;
    }

    public DirectorResponse patchDirector(Long id, PatchDirectorRequest request) {
        DirectorResponse existing = findById(id);
        String newFirstName = request.firstName() != null ? request.firstName() : existing.getFirstName();
        String newLastName = request.lastName() != null ? request.lastName() : existing.getLastName();
        DirectorResponse updated = DirectorResponse.builder()
                .id(id)
                .firstName(newFirstName)
                .lastName(newLastName)
                .fullName(newFirstName + " " + newLastName)
                .email(request.email() != null ? request.email() : existing.getEmail())
                .bio(request.bio() != null ? request.bio() : existing.getBio())
                .birthDate(request.birthDate() != null ? request.birthDate() : existing.getBirthDate())
                .nationality(request.nationality() != null ? request.nationality() : existing.getNationality())
                .filmsCount(existing.getFilmsCount())
                .build();
        storage.directors.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        DirectorResponse director = findById(id);

        int filmsCount = (int) storage.films.values().stream()
                .filter(b -> b.getDirector() != null && b.getDirector().getId().equals(id))
                .count();

        filmService.deleteFilmsByDirectorId(id);
        storage.directors.remove(id);
        eventPublisher.publishDeleted(director, filmsCount);
    }
}
