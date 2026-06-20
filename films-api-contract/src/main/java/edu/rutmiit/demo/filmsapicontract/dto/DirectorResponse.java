package edu.rutmiit.demo.filmsapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Getter
@Builder
    @EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "directors", itemRelation = "director")
@Schema(description = "Информация об режиссёре")
public class DirectorResponse extends RepresentationModel<DirectorResponse> {

    @Schema(description = "Уникальный идентификатор режиссёра", example = "1")
    private final Long id;

    @Schema(description = "Имя режиссёра", example = "Лев")
    private final String firstName;

    @Schema(description = "Фамилия режиссёра", example = "Толстой")
    private final String lastName;

    @Schema(description = "Полное имя (firstName + lastName)", example = "Лев Толстой")
    private final String fullName;

    @Schema(description = "Email режиссёра", example = "tolstoy@example.com")
    private final String email;

    @Schema(description = "Краткая биография режиссёра")
    private final String bio;

    @Schema(description = "Дата рождения режиссёра", example = "1828-09-09")
    private final LocalDate birthDate;

    @Schema(description = "Национальность режиссёра", example = "Русский")
    private final String nationality;

    @Schema(description = "Общее количество фильмов режиссёра в каталоге", example = "3")
    private final Integer filmsCount;
}
