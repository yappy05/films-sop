package edu.rutmiit.demo.restservice.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;
import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;
import edu.rutmiit.demo.restservice.service.DirectorService;

/**
 * Вложенный резолвер для поля Film.director.
 *
 * В GraphQL каждое поле может иметь свой резолвер. Когда клиент запрашивает
 * фильм вместе с режиссёром:
 *
 *   query {
 *     film(id: "1") {
 *       title
 *       director {       ← этот резолвер срабатывает
 *         fullName
 *       }
 *     }
 *   }
 *
 * DGS вызывает этот метод для каждой фильмы, передавая родительский объект
 * через DgsDataFetchingEnvironment. Если клиент НЕ запросил поле director,
 * этот резолвер вообще не вызывается — экономия ресурсов.
 *
 * Аннотация @DgsData(parentType, field) привязывает метод к конкретному полю
 * конкретного типа в GraphQL-схеме.
 */
@DgsComponent
public class FilmDirectorDataFetcher {

    private final DirectorService directorService;

    public FilmDirectorDataFetcher(DirectorService directorService) {
        this.directorService = directorService;
    }

    /**
     * Загружает режиссёра для заданной фильмы.
     *
     * Родительский объект (Film) извлекается из DgsDataFetchingEnvironment.
     * В нашем in-memory хранилище режиссёр уже вложен в FilmResponse,
     * поэтому мы просто его возвращаем. В реальном проекте здесь был бы
     * вызов к базе данных или внешнему сервису.
     */
    @DgsData(parentType = "Film", field = "director")
    public DirectorResponse director(DgsDataFetchingEnvironment dfe) {
        FilmResponse film = dfe.getSource();

        // Если режиссёр уже вложен в FilmResponse, возвращаем его напрямую.
        // В реальном приложении здесь мог бы быть вызов directorService.findById().
        if (film.getDirector() != null) {
            return film.getDirector();
        }

        // Запасной вариант — загрузить режиссёра отдельно (для демонстрации)
        return null;
    }
}
