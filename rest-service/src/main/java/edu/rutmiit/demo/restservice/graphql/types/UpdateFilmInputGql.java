package edu.rutmiit.demo.restservice.graphql.types;

public record UpdateFilmInputGql(
        String title,
        String imdbId,
        String description,
        String genre,
        Integer publishedYear,
        String language
) {}
