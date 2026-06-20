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

@DgsComponent
public class FilmDataFetcher {

    private final FilmService filmService;

    public FilmDataFetcher(FilmService filmService) {
        this.filmService = filmService;
    }

    @DgsQuery
    public FilmResponse film(@InputArgument String id) {
        return filmService.findFilmById(Long.parseLong(id));
    }

    @DgsQuery
    public FilmConnectionGql films(
            @InputArgument FilmFilterGql filter,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

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

        PagedResponse<FilmResponse> paged = filmService.findAllFilms(
                directorId, genre, publishedYear, titleSearch, pageNum, pageSize);

        return new FilmConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }

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

    @DgsMutation
    public boolean deleteFilm(@InputArgument String id) {
        filmService.deleteFilm(Long.parseLong(id));
        return true;
    }
}
