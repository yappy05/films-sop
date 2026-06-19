package edu.rutmiit.demo.events;

/**
 * Семейство событий, связанных с режиссёрами.
 *
 * Аналогично FilmEvent — sealed interface гарантирует полный перечень вариантов.
 * Десериализация по eventType, а не через Jackson-аннотации.
 */
public sealed interface DirectorEvent {

    /**
     * Режиссёр создан. Содержит основные атрибуты нового режиссёра.
     */
    record Created(
            Long directorId,
            String firstName,
            String lastName,
            String fullName,
            String nationality
    ) implements DirectorEvent {}

    /**
     * Режиссёр удалён. В нашей системе удаление каскадное — вместе с фильмми.
     */
    record Deleted(
            Long directorId,
            String fullName,
            int deletedFilmsCount
    ) implements DirectorEvent {}
}
