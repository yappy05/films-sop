package edu.rutmiit.demo.restservice.graphql.types;

import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;

import java.util.List;

/**
 * Тип-обёртка для постраничного ответа со списком фильмов.
 * Соответствует типу FilmConnection в GraphQL-схеме.
 */
public record FilmConnectionGql(
        List<FilmResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}
