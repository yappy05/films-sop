package edu.rutmiit.demo.restservice.controllers;

import edu.rutmiit.demo.filmsapicontract.dto.*;
import edu.rutmiit.demo.filmsapicontract.endpoints.FilmApi;
import edu.rutmiit.demo.restservice.assemblers.FilmModelAssembler;
import edu.rutmiit.demo.restservice.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmController implements FilmApi {

    private final FilmService filmService;
    private final FilmModelAssembler filmModelAssembler;
    private final PagedResourcesAssembler<FilmResponse> pagedResourcesAssembler;

    public FilmController(FilmService filmService, FilmModelAssembler filmModelAssembler,
                          PagedResourcesAssembler<FilmResponse> pagedResourcesAssembler) {
        this.filmService = filmService;
        this.filmModelAssembler = filmModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EntityModel<FilmResponse> getFilmById(Long id) {
        return filmModelAssembler.toModel(filmService.findFilmById(id));
    }

    @Override
    public PagedModel<EntityModel<FilmResponse>> getAllFilms(Long directorId, String genre, Integer publishedYear,
                                                            String titleSearch, int page, int size) {
        PagedResponse<FilmResponse> paged = filmService.findAllFilms(directorId, genre, publishedYear, titleSearch, page, size);
        Page<FilmResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedResourcesAssembler.toModel(springPage, filmModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<FilmResponse>> createFilm(FilmRequest request) {
        FilmResponse created = filmService.createFilm(request);
        EntityModel<FilmResponse> model = filmModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<FilmResponse> updateFilm(Long id, UpdateFilmRequest request) {
        return filmModelAssembler.toModel(filmService.updateFilm(id, request));
    }

    @Override
    public EntityModel<FilmResponse> patchFilm(Long id, PatchFilmRequest request) {
        return filmModelAssembler.toModel(filmService.patchFilm(id, request));
    }

    @Override
    public void deleteFilm(Long id) {
        filmService.deleteFilm(id);
    }
}
