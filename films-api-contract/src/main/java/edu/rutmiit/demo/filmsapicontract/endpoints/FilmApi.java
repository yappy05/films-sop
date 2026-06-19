package edu.rutmiit.demo.filmsapicontract.endpoints;

import edu.rutmiit.demo.filmsapicontract.config.FilmsApiContractConfig;
import edu.rutmiit.demo.filmsapicontract.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контракт API для управления фильмми.
 * Реализующий контроллер в сервисе должен имплементировать этот интерфейс.
 */
@Tag(name = "Films", description = "Управление фильмми в каталоге")
@RequestMapping(
        value = "/api/films",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface FilmApi {

    @Operation(
            summary = "Получить фильм по ID",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Фильм найдена")
    @ApiResponse(responseCode = "404", description = "Фильм не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<FilmResponse> getFilmById(
            @Parameter(description = "ID фильма", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Список фильмов",
            description = """
                    Возвращает постраничный список фильмов с HATEOAS-ссылками.
                    Поддерживает комбинирование фильтров: directorId, genre, publishedYear и titleSearch
                    можно передавать одновременно.
                    """,
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Постраничный список фильмов")
    @GetMapping
    PagedModel<EntityModel<FilmResponse>> getAllFilms(
            @Parameter(description = "Фильтр по ID режиссёра") @RequestParam(required = false) Long directorId,
            @Parameter(description = "Фильтр по жанру", example = "Роман") @RequestParam(required = false) String genre,
            @Parameter(description = "Фильтр по году публикации", example = "2010") @RequestParam(required = false) Integer publishedYear,
            @Parameter(description = "Поиск по названию (substring, case-insensitive)", example = "Начало") @RequestParam(required = false) String titleSearch,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Создать фильм",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Фильм создан. Location header содержит URI нового ресурса.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Режиссёр с указанным directorId не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Фильм с таким IMDb ID уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<FilmResponse>> createFilm(@Valid @RequestBody FilmRequest request);

    @Operation(
            summary = "Полное обновление фильма (PUT)",
            description = "Заменяет все поля фильма. Режиссёра изменить нельзя. "
                    + "Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Фильм обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Фильм не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Фильм с таким IMDb ID уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<FilmResponse> updateFilm(
            @Parameter(description = "ID фильма", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateFilmRequest request
    );

    @Operation(
            summary = "Частичное обновление фильма (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений. Режиссёра фильмы изменить нельзя.
                    """,
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Фильм обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Фильм не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Фильм с таким IMDb ID уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<FilmResponse> patchFilm(
            @Parameter(description = "ID фильма", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchFilmRequest request
    );

    @Operation(
            summary = "Удалить фильм",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Фильм удалён")
    @ApiResponse(responseCode = "404", description = "Фильм не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFilm(
            @Parameter(description = "ID фильма", required = true, example = "1") @PathVariable Long id
    );
}

  