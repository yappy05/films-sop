package edu.rutmiit.demo.restservice.assemblers;

import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;
import edu.rutmiit.demo.restservice.controllers.DirectorController;
import edu.rutmiit.demo.restservice.controllers.FilmController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DirectorModelAssembler implements RepresentationModelAssembler<DirectorResponse, EntityModel<DirectorResponse>> {

    @Override
    public EntityModel<DirectorResponse> toModel(DirectorResponse director) {
        return EntityModel.of(director,
                linkTo(methodOn(DirectorController.class).getDirectorById(director.getId())).withSelfRel(),
                linkTo(methodOn(FilmController.class).getAllFilms(director.getId(), null, null, null, 0, 20)).withRel("films"),
                linkTo(methodOn(DirectorController.class).getAllDirectors(0, 20)).withRel("collection")
        );
    }
}