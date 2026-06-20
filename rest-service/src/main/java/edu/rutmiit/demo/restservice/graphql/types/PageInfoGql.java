package edu.rutmiit.demo.restservice.graphql.types;

public record PageInfoGql(
        int pageNumber,
        int pageSize,
        int totalPages,
        boolean last
) {}
