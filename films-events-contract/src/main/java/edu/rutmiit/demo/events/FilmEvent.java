package edu.rutmiit.demo.events;

/**
 * Семейство событий, связанных с фильмми.
 *
 * Sealed interface (Java 17+) ограничивает набор наследников — компилятор гарантирует,
 * что FilmEvent может быть ТОЛЬКО Created, Updated или Deleted.
 * Это делает switch/pattern matching исчерпывающим (exhaustive):
 *
 *   switch (event) {
 *       case FilmEvent.Created c -> ...
 *       case FilmEvent.Updated u -> ...
 *       case FilmEvent.Deleted d -> ...
 *       // компилятор знает, что других вариантов нет
 *   }
 *
 * Полиморфная десериализация выполняется не через Jackson-аннотации,
 * а через поле eventType в EventMetadata — consumer определяет конкретный тип
 * по routing key и десериализует payload в нужный record напрямую.
 */
public sealed interface FilmEvent {

    /**
     * Фильм создан. Содержит все ключевые атрибуты нового фильма.
     */
    record Created(
            Long filmId,
            String title,
            String imdbId,
            Long directorId,
            String directorFullName,
            String genre,
            Integer publishedYear
    ) implements FilmEvent {}

    /**
     * Фильм обновлён. Содержит актуальное состояние после обновления.
     * В промышленных системах здесь могут быть и старые значения (before/after),
     * но для демонстрации достаточно нового состояния.
     */
    record Updated(
            Long filmId,
            String title,
            String imdbId,
            String genre,
            Integer publishedYear
    ) implements FilmEvent {}

    /**
     * Фильм удалён. Достаточно идентификатора — после удаления данных нет.
     */
    record Deleted(
            Long filmId,
            String title
    ) implements FilmEvent {}

    /**
     * Фильм обогащён аналитикой — результат gRPC-вызова к analytics-серверу.
     *
     * Событие публикуется grpc-enrichment-client после получения ответа
     * от grpc-analytics-server. Содержит вычисленные метрики фильма.
     */
    record Enriched(
            Long filmId,
            String title,
            int estimatedReadingMinutes,
            String difficultyLevel,
            double recommendationScore,
            String eraClassification
    ) implements FilmEvent {}
}
