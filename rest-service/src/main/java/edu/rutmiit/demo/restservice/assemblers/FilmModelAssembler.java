package edu.rutmiit.demo.restservice.assemblers;

import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;
import edu.rutmiit.demo.restservice.controllers.DirectorController;
import edu.rutmiit.demo.restservice.controllers.FilmController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FilmModelAssembler implements RepresentationModelAssembler<FilmResponse, EntityModel<FilmResponse>> {

    @Override
    public EntityModel<FilmResponse> toModel(FilmResponse film) {
        EntityModel<FilmResponse> model = EntityModel.of(film,
                linkTo(methodOn(FilmController.class).getFilmById(film.getId())).withSelfRel(),
                linkTo(methodOn(FilmController.class).getAllFilms(null, null, null, null, 0, 20)).withRel("collection")
        );
        if (film.getDirector() != null) {
            model.add(linkTo(methodOn(DirectorController.class)
                    .getDirectorById(film.getDirector().getId())).withRel("director"));
        }
        return model;
    }
}
