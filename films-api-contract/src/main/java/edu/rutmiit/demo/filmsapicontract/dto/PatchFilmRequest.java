package edu.rutmiit.demo.filmsapicontract.dto;

import edu.rutmiit.demo.filmsapicontract.validation.ValidImdbId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Запрос для частичного обновления фильма (PATCH, семантика JSON Merge Patch).
 *
 * Передайте только те поля, которые нужно изменить.
 * Поля, отсутствующие в запросе, десериализуются как null — сервис их не трогает.
 *
 * Ограничение: стандартный Jackson не различает «поле не пришло» и «пришло явно null».
 * В этом контракте оба случая означают «не менять». Для точного различения
 * можно использовать JsonNullable из библиотеки jackson-databind-nullable.
 *
 * Сменить режиссёра через PATCH нельзя — для этого создайте новый фильм.
 */
@Schema(description = "Частичное обновление фильма (PATCH). Передайте только те поля, которые нужно изменить. "
        + "Непереданные поля остаются без изменений.")
public record PatchFilmRequest(

        @Schema(description = "Новое название фильма", example = "Начало (Director's Cut)")
        @Size(max = 500, message = "Название не может превышать 500 символов")
        String title,

        @Schema(description = "Новый IMDb ID (формат tt + 7–8 цифр)", example = "tt1375666")
        @ValidImdbId
        String imdbId,

        @Schema(description = "Новое описание фильма")
        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        String description,

        @Schema(description = "Новый жанр фильма", example = "Научная фантастика")
        @Size(max = 100, message = "Жанр не может превышать 100 символов")
        String genre,

        @Schema(description = "Новый год публикации", example = "2010")
        @Min(value = 1, message = "Год выхода должен быть положительным")
        @Max(value = 9999, message = "Укажите корректный год публикации")
        Integer publishedYear,

        @Schema(description = "Новый язык фильма (код ISO 639-1)", example = "ru")
        @Size(min = 2, max = 5, message = "Код языка должен содержать 2-5 символов")
        String language
) {}
