package edu.rutmiit.demo.restservice.graphql.types;

public record CreateFilmInputGql(
        String title,
        String imdbId,
        String directorId,
        String description,
        String genre,
        Integer publishedYear,
        String language
) {}
