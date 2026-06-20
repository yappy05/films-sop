package edu.rutmiit.demo.restservice.graphql.types;

import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;

import java.util.List;

public record FilmConnectionGql(
        List<FilmResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}
