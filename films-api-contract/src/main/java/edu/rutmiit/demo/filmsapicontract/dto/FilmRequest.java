package edu.rutmiit.demo.filmsapicontract.dto;

import edu.rutmiit.demo.filmsapicontract.validation.ValidImdbId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO для создания нового фильма.
 */
@Schema(description = "Запрос на создание фильма")
public record FilmRequest(

        @Schema(description = "Название фильма", example = "Начало", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Название не может быть пустым")
        @Size(max = 500, message = "Название не может превышать 500 символов")
        String title,

        @Schema(description = "IMDb ID фильма (формат tt + 7–8 цифр)", example = "tt1375666", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "IMDb ID не может быть пустым")
        @ValidImdbId
        String imdbId,

        @Schema(description = "ID режиссёра фильма", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID режиссёра не может быть пустым")
        Long directorId,

        @Schema(description = "Краткое описание фильма", example = "Шпион проникает в сны людей, чтобы украсть или внедрить идеи.")
        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        String description,

        @Schema(description = "Жанр фильма", example = "Научная фантастика")
        @Size(max = 100, message = "Жанр не может превышать 100 символов")
        String genre,

        @Schema(description = "Год выхода", example = "2010")
        @Min(value = 1, message = "Год выхода должен быть положительным")
        @Max(value = 9999, message = "Укажите корректный год публикации")
        Integer publishedYear,

        @Schema(description = "Язык фильма (код ISO 639-1)", example = "ru")
        @Size(min = 2, max = 5, message = "Код языка должен содержать 2-5 символов")
        String language
) {}