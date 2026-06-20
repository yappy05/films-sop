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

@Tag(name = "Directors", description = "Управление режиссёрами книжного каталога")
@RequestMapping(
        value = "/api/directors",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface DirectorApi {

    @Operation(
            summary = "Список режиссёров",
            description = "Возвращает постраничный список режиссёров с HATEOAS-ссылками. "
                    + "Ссылки prev/next позволяют клиенту навигировать по страницам без знания офсетов.",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список режиссёров")
    @GetMapping
    PagedModel<EntityModel<DirectorResponse>> getAllDirectors(
            @Parameter(description = "Номер страницы (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Получить режиссёра по ID",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Режиссёр найден")
    @ApiResponse(responseCode = "404", description = "Режиссёр не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<DirectorResponse> getDirectorById(
            @Parameter(description = "ID режиссёра", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Создать режиссёра",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Режиссёр создан. Location header содержит URI нового ресурса.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<DirectorResponse>> createDirector(@Valid @RequestBody DirectorRequest request);

    @Operation(
            summary = "Полное обновление режиссёра (PUT)",
            description = "Заменяет все поля режиссёра. Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Режиссёр обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Режиссёр не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<DirectorResponse> updateDirector(
            @Parameter(description = "ID режиссёра", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody DirectorRequest request
    );

    @Operation(
            summary = "Частичное обновление режиссёра (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений.
                    """,
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Режиссёр обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Режиссёр не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<DirectorResponse> patchDirector(
            @Parameter(description = "ID режиссёра", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchDirectorRequest request
    );

    @Operation(
            summary = "Удалить режиссёра",
            description = "Удаляет режиссёра и все его фильмы (каскадное удаление).",
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Режиссёр удалён")
    @ApiResponse(responseCode = "404", description = "Режиссёр не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDirector(
            @Parameter(description = "ID режиссёра", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Фильмы режиссёра (суб-ресурс)",
            description = """
                    Возвращает постраничный список фильмов указанного режиссёра.
                    Это суб-ресурс (концепция REST): /directors/{id}/films.
                    Эквивалентен GET /films?directorId={id}, но точнее отражает иерархию.
                    """,
            security = @SecurityRequirement(name = FilmsApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список фильмов режиссёра")
    @ApiResponse(responseCode = "404", description = "Режиссёр не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/films")
    PagedModel<EntityModel<FilmResponse>> getFilmsByDirector(
            @Parameter(description = "ID режиссёра", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );
}
