package edu.rutmiit.demo.events;

/**
 * Константы для маршрутизации событий в RabbitMQ.
 *
 * Routing key в topic exchange работает как почтовый индекс:
 * - "film.created" — конкретное событие
 * - "film.*"       — все события фильмов
 * - "#"            — все события вообще
 *
 * Вынесены в контракт, чтобы publisher и consumer использовали одни и те же строки.
 * Рассогласование routing key — частая ошибка, которую трудно отследить.
 */
public final class RoutingKeys {

    private RoutingKeys() {
        // утилитарный класс — экземпляры не создаём
    }

    // Имя общего topic exchange для доменных событий
    public static final String EXCHANGE = "films.events";

    // Routing keys для событий фильмов
    public static final String FILM_CREATED = "film.created";
    public static final String FILM_UPDATED = "film.updated";
    public static final String FILM_DELETED = "film.deleted";
    public static final String FILM_ENRICHED = "film.enriched";

    // Routing keys для событий режиссёров
    public static final String DIRECTOR_CREATED = "director.created";
    public static final String DIRECTOR_DELETED = "director.deleted";

    // Паттерны для подписки (wildcard)
    public static final String ALL_BOOK_EVENTS = "film.*";
    public static final String ALL_AUTHOR_EVENTS = "director.*";
    public static final String ALL_EVENTS = "#";
}
