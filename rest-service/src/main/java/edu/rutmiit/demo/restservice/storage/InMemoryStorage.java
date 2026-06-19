package edu.rutmiit.demo.restservice.storage;

import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;
import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, DirectorResponse> directors = new ConcurrentHashMap<>();
    public final Map<Long, FilmResponse> films = new ConcurrentHashMap<>();

    public final AtomicLong directorSequence = new AtomicLong(0);
    public final AtomicLong filmSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        DirectorResponse director1 = DirectorResponse.builder()
                .id(directorSequence.incrementAndGet())
                .firstName("Кристофер")
                .lastName("Нолан")
                .fullName("Кристофер Нолан")
                .nationality("Британский")
                .birthDate(LocalDate.of(1970, 7, 30))
                .bio("Режиссёр и сценарист, известный сложными сюжетами и работой с IMAX.")
                .filmsCount(2)
                .build();

        DirectorResponse director2 = DirectorResponse.builder()
                .id(directorSequence.incrementAndGet())
                .firstName("Квентин")
                .lastName("Тарантино")
                .fullName("Квентин Тарантино")
                .nationality("Американский")
                .birthDate(LocalDate.of(1963, 3, 27))
                .bio("Режиссёр, сценарист и продюсер, автор культовых фильмов 90-х и 2000-х.")
                .filmsCount(1)
                .build();

        directors.put(director1.getId(), director1);
        directors.put(director2.getId(), director2);

        long filmId1 = filmSequence.incrementAndGet();
        films.put(filmId1, FilmResponse.builder()
                .id(filmId1)
                .title("Начало")
                .imdbId("tt1375666")
                .director(director1)
                .genre("Научная фантастика")
                .publishedYear(2010)
                .language("en")
                .description("Шпион проникает в сны людей, чтобы украсть или внедрить идеи.")
                .createdAt(LocalDateTime.now())
                .build());

        long filmId2 = filmSequence.incrementAndGet();
        films.put(filmId2, FilmResponse.builder()
                .id(filmId2)
                .title("Тёмный рыцарь")
                .imdbId("tt0468569")
                .director(director1)
                .genre("Боевик")
                .publishedYear(2008)
                .language("en")
                .description("Бэтмен противостоит Джокеру, который сеет хаос в Готэме.")
                .createdAt(LocalDateTime.now())
                .build());

        long filmId3 = filmSequence.incrementAndGet();
        films.put(filmId3, FilmResponse.builder()
                .id(filmId3)
                .title("Криминальное чтиво")
                .imdbId("tt0110912")
                .director(director2)
                .genre("Криминал")
                .publishedYear(1994)
                .language("en")
                .description("Переплетённые истории гангстеров, боксёра и грабителей.")
                .createdAt(LocalDateTime.now())
                .build());
    }
}
