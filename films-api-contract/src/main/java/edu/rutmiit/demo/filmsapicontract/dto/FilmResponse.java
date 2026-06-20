package edu.rutmiit.demo.filmsapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "films", itemRelation = "film")
@Schema(description = "Информация о фильме")
public class FilmResponse extends RepresentationModel<FilmResponse> {

    @Schema(description = "Уникальный идентификатор фильма", example = "1")
    private final Long id;

    @Schema(description = "Название фильма", example = "Начало")
    private final String title;

    @Schema(description = "IMDb ID фильма", example = "tt1375666")
    private final String imdbId;

    @Schema(description = "Режиссёр фильма")
    private final DirectorResponse director;

    @Schema(description = "Краткое описание фильма")
    private final String description;

    @Schema(description = "Жанр фильма", example = "Научная фантастика")
    private final String genre;

    @Schema(description = "Год выхода", example = "2010")
    private final Integer publishedYear;

    @Schema(description = "Язык фильма (ISO 639-1)", example = "ru")
    private final String language;

    @Schema(description = "Момент создания записи в каталоге")
    private final LocalDateTime createdAt;

    @Schema(description = "Момент последнего обновления записи")
    private final LocalDateTime updatedAt;
}
