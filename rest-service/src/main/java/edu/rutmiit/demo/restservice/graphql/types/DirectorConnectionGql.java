package edu.rutmiit.demo.restservice.graphql.types;

import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;

import java.util.List;

public record DirectorConnectionGql(
        List<DirectorResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}
