package edu.rutmiit.demo.events;

public sealed interface FilmEvent {

    record Created(
            Long filmId,
            String title,
            String imdbId,
            Long directorId,
            String directorFullName,
            String genre,
            Integer publishedYear
    ) implements FilmEvent {}

    record Updated(
            Long filmId,
            String title,
            String imdbId,
            String genre,
            Integer publishedYear
    ) implements FilmEvent {}

    record Deleted(
            Long filmId,
            String title
    ) implements FilmEvent {}

    record Enriched(
            Long filmId,
            String title,
            int estimatedReadingMinutes,
            String difficultyLevel,
            double recommendationScore,
            String eraClassification
    ) implements FilmEvent {}
}
