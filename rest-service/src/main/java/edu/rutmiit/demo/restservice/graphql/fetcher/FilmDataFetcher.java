package edu.rutmiit.demo.restservice.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.filmsapicontract.dto.FilmRequest;
import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;
import edu.rutmiit.demo.filmsapicontract.dto.PagedResponse;
import edu.rutmiit.demo.filmsapicontract.dto.UpdateFilmRequest;
import edu.rutmiit.demo.restservice.graphql.types.FilmConnectionGql;
import edu.rutmiit.demo.restservice.graphql.types.FilmFilterGql;
import edu.rutmiit.demo.restservice.graphql.types.CreateFilmInputGql;
import edu.rutmiit.demo.restservice.graphql.types.PageInfoGql;
import edu.rutmiit.demo.restservice.graphql.types.UpdateFilmInputGql;
import edu.rutmiit.demo.restservice.service.FilmService;

/**
 * DataFetcher для операций с фильмми.
 *
 * Аннотация @DgsComponent регистрирует этот класс как компонент DGS-фреймворка.
 * Каждый метод с @DgsQuery или @DgsMutation привязывается к соответствующему полю
 * в GraphQL-схеме. DGS находит их по имени метода (или по явному параметру field).
 *
 * Этот DataFetcher обрабатывает корневые поля Query и Mutation для фильмов.
 * Вложенные поля (Film.director) обрабатываются в отдельном резолвере.
 */
@DgsComponent
public class FilmDataFetcher {

    private final FilmService filmService;

    public FilmDataFetcher(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Получение фильмы по идентификатору.
     * Соответствует полю Query.film(id: ID!) в схеме.
     * Возвращает null если фильм не найдена (вместо исключения, как принято в GraphQL).
     */
    @DgsQuery
    public FilmResponse film(@InputArgument String id) {
        return filmService.findFilmById(Long.parseLong(id));
    }

    /**
     * Список фильмов с фильтрацией и пагинацией.
     * Соответствует полю Query.films(filter, page, size) в схеме.
     *
     * @InputArgument автоматически маппит GraphQL-аргументы на Java-параметры.
     * Для сложных типов (input FilmFilter) DGS сам десериализует JSON в объект.
     */
    @DgsQuery
    public FilmConnectionGql films(
            @InputArgument FilmFilterGql filter,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        // Подставляем значения по умолчанию, если клиент не передал аргументы
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        // Извлекаем параметры фильтрации
        Long directorId = null;
        String genre = null;
        Integer publishedYear = null;
        String titleSearch = null;

        if (filter != null) {
            directorId = filter.directorId() != null ? Long.parseLong(filter.directorId()) : null;
            genre = filter.genre();
            publishedYear = filter.publishedYear();
            titleSearch = filter.titleSearch();
        }

        // Переиспользуем существующий сервисный слой — GraphQL не дублирует бизнес-логику
        PagedResponse<FilmResponse> paged = filmService.findAllFilms(
                directorId, genre, publishedYear, titleSearch, pageNum, pageSize);

        return new FilmConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }

    /**
     * Создание фильмы.
     * Соответствует полю Mutation.createFilm(input: CreateFilmInput!) в схеме.
     */
    @DgsMutation
    public FilmResponse createFilm(@InputArgument CreateFilmInputGql input) {
        FilmRequest request = new FilmRequest(
                input.title(),
                input.imdbId(),
                Long.parseLong(input.directorId()),
                input.description(),
                input.genre(),
                input.publishedYear(),
                input.language()
        );
        return filmService.createFilm(request);
    }

    /**
     * Обновление фильмы.
     * Соответствует полю Mutation.updateFilm(id, input) в схеме.
     */
    @DgsMutation
    public FilmResponse updateFilm(@InputArgument String id, @InputArgument UpdateFilmInputGql input) {
        UpdateFilmRequest request = new UpdateFilmRequest(
                input.title(),
                input.imdbId(),
                input.description(),
                input.genre(),
                input.publishedYear(),
                input.language()
        );
        return filmService.updateFilm(Long.parseLong(id), request);
    }

    /**
     * Удаление фильмы.
     * Соответствует полю Mutation.deleteFilm(id) в схеме.
     * Возвращает true при успешном удалении.
     */
    @DgsMutation
    public boolean deleteFilm(@InputArgument String id) {
        filmService.deleteFilm(Long.parseLong(id));
        return true;
    }
}
