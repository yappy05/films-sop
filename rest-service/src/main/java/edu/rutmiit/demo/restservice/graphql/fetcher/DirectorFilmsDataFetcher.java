package edu.rutmiit.demo.restservice.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;
import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;
import edu.rutmiit.demo.filmsapicontract.dto.PagedResponse;
import edu.rutmiit.demo.restservice.graphql.types.FilmConnectionGql;
import edu.rutmiit.demo.restservice.graphql.types.PageInfoGql;
import edu.rutmiit.demo.restservice.service.FilmService;

/**
 * Вложенный резолвер для поля Director.films.
 *
 * Срабатывает когда клиент запрашивает фильмы режиссёра:
 *
 *   query {
 *     director(id: "1") {
 *       fullName
 *       films(page: 0, size: 5) {    ← этот резолвер
 *         content {
 *           title
 *         }
 *         totalElements
 *       }
 *     }
 *   }
 *
 * Демонстрирует работу с аргументами вложенного поля (page, size)
 * и доступ к родительскому объекту (Director).
 */
@DgsComponent
public class DirectorFilmsDataFetcher {

    private final FilmService filmService;

    public DirectorFilmsDataFetcher(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Загружает фильмы указанного режиссёра с пагинацией.
     *
     * Аргументы (page, size) берутся из GraphQL-запроса через @InputArgument.
     * Родительский объект (Director) берётся из DgsDataFetchingEnvironment.
     */
    @DgsData(parentType = "Director", field = "films")
    public FilmConnectionGql films(
            DgsDataFetchingEnvironment dfe,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        DirectorResponse director = dfe.getSource();

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        // Фильтруем фильмы по ID режиссёра — переиспользуем сервис
        PagedResponse<FilmResponse> paged = filmService.findAllFilms(
                director.getId(), null, null, null, pageNum, pageSize);

        return new FilmConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }
}
