package edu.rutmiit.demo.restservice.graphql.types;

/**
 * Входной тип для создания фильмы.
 * Соответствует input CreateFilmInput в GraphQL-схеме.
 */
public record CreateFilmInputGql(
        String title,
        String imdbId,
        String directorId,
        String description,
        String genre,
        Integer publishedYear,
        String language
) {}
