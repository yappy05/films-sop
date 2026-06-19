package edu.rutmiit.demo.restservice.graphql.types;

import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;

import java.util.List;

/**
 * Тип-обёртка для постраничного ответа со списком режиссёров.
 * Соответствует типу DirectorConnection в GraphQL-схеме.
 */
public record DirectorConnectionGql(
        List<DirectorResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}
