package edu.rutmiit.demo.restservice.graphql.types;

/**
 * Входной тип для обновления фильма.
 * Соответствует input UpdateFilmInput в GraphQL-схеме.
 * Режиссёра изменить нельзя.
 */
public record UpdateFilmInputGql(
        String title,
        String imdbId,
        String description,
        String genre,
        Integer publishedYear,
        String language
) {}
