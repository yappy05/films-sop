package edu.rutmiit.demo.restservice.graphql.types;

/**
 * Входной тип для фильтрации фильмов.
 * Соответствует input FilmFilter в GraphQL-схеме.
 *
 * Все поля необязательны — клиент передаёт только нужные фильтры.
 */
public record FilmFilterGql(
        String directorId,
        String genre,
        Integer publishedYear,
        String titleSearch
) {}
