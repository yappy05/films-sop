package edu.rutmiit.demo.restservice.controllers;

import edu.rutmiit.demo.filmsapicontract.dto.*;
import edu.rutmiit.demo.filmsapicontract.endpoints.DirectorApi;
import edu.rutmiit.demo.restservice.assemblers.DirectorModelAssembler;
import edu.rutmiit.demo.restservice.assemblers.FilmModelAssembler;
import edu.rutmiit.demo.restservice.service.DirectorService;
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
public class DirectorController implements DirectorApi {

    private final DirectorService directorService;
    private final FilmService filmService;
    private final DirectorModelAssembler directorModelAssembler;
    private final FilmModelAssembler filmModelAssembler;
    private final PagedResourcesAssembler<DirectorResponse> pagedDirectorsAssembler;
    private final PagedResourcesAssembler<FilmResponse> pagedFilmsAssembler;

    public DirectorController(DirectorService directorService,
                            FilmService filmService,
                            DirectorModelAssembler directorModelAssembler,
                            FilmModelAssembler filmModelAssembler,
                            PagedResourcesAssembler<DirectorResponse> pagedDirectorsAssembler,
                            PagedResourcesAssembler<FilmResponse> pagedFilmsAssembler) {
        this.directorService = directorService;
        this.filmService = filmService;
        this.directorModelAssembler = directorModelAssembler;
        this.filmModelAssembler = filmModelAssembler;
        this.pagedDirectorsAssembler = pagedDirectorsAssembler;
        this.pagedFilmsAssembler = pagedFilmsAssembler;
    }

    @Override
    public PagedModel<EntityModel<DirectorResponse>> getAllDirectors(int page, int size) {
        PagedResponse<DirectorResponse> paged = directorService.findAll(page, size);
        Page<DirectorResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedDirectorsAssembler.toModel(springPage, directorModelAssembler);
    }

    @Override
    public EntityModel<DirectorResponse> getDirectorById(Long id) {
        return directorModelAssembler.toModel(directorService.findById(id));
    }

    @Override
    public ResponseEntity<EntityModel<DirectorResponse>> createDirector(DirectorRequest request) {
        DirectorResponse created = directorService.create(request);
        EntityModel<DirectorResponse> model = directorModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<DirectorResponse> updateDirector(Long id, DirectorRequest request) {
        return directorModelAssembler.toModel(directorService.update(id, request));
    }

    @Override
    public EntityModel<DirectorResponse> patchDirector(Long id, PatchDirectorRequest request) {
        return directorModelAssembler.toModel(directorService.patchDirector(id, request));
    }

    @Override
    public void deleteDirector(Long id) {
        directorService.delete(id);
    }

    @Override
    public PagedModel<EntityModel<FilmResponse>> getFilmsByDirector(Long id, int page, int size) {

        directorService.findById(id);
        PagedResponse<FilmResponse> paged = filmService.findAllFilms(id, null, null, null, page, size);
        Page<FilmResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedFilmsAssembler.toModel(springPage, filmModelAssembler);
    }
}
