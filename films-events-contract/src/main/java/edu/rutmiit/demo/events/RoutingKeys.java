package edu.rutmiit.demo.events;

public final class RoutingKeys {

    private RoutingKeys() {

    }

    public static final String EXCHANGE = "films.events";

    public static final String FILM_CREATED = "film.created";
    public static final String FILM_UPDATED = "film.updated";
    public static final String FILM_DELETED = "film.deleted";
    public static final String FILM_ENRICHED = "film.enriched";

    public static final String DIRECTOR_CREATED = "director.created";
    public static final String DIRECTOR_DELETED = "director.deleted";

    public static final String ALL_BOOK_EVENTS = "film.*";
    public static final String ALL_AUTHOR_EVENTS = "director.*";
    public static final String ALL_EVENTS = "#";
}
