package edu.rutmiit.demo.restservice.graphql.types;

public record FilmFilterGql(
        String directorId,
        String genre,
        Integer publishedYear,
        String titleSearch
) {}
