package edu.rutmiit.demo.filmsapicontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO для создания или полного обновления режиссёра (POST / PUT).
 * Все обязательные поля должны присутствовать.
 */
@Schema(description = "Запрос на создание или полное обновление режиссёра")
public record DirectorRequest(

        @Schema(description = "Имя режиссёра", example = "Лев", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя режиссёра не может быть пустым")
        @Size(max = 100, message = "Имя не может превышать 100 символов")
        String firstName,

        @Schema(description = "Фамилия режиссёра", example = "Толстой", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия режиссёра не может быть пустой")
        @Size(max = 100, message = "Фамилия не может превышать 100 символов")
        String lastName,

        @Schema(description = "Email режиссёра", example = "tolstoy@example.com")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Краткая биография режиссёра", example = "Русский писатель, один из величайших в мировой литературе.")
        @Size(max = 2000, message = "Биография не может превышать 2000 символов")
        String bio,

        @Schema(description = "Дата рождения режиссёра", example = "1828-09-09")
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthDate,

        @Schema(description = "Национальность режиссёра", example = "Русский")
        @Size(max = 100, message = "Национальность не может превышать 100 символов")
        String nationality
) {}