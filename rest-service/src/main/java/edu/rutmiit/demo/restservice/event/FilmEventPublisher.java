package edu.rutmiit.demo.restservice.event;

import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;
import edu.rutmiit.demo.events.FilmEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FilmEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(FilmEventPublisher.class);
    private static final String SOURCE = "rest-service";

    private final RabbitTemplate rabbitTemplate;

    public FilmEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(FilmResponse film) {
        var event = new FilmEvent.Created(
                film.getId(),
                film.getTitle(),
                film.getImdbId(),
                film.getDirector() != null ? film.getDirector().getId() : null,
                film.getDirector() != null ? film.getDirector().getFullName() : "Неизвестен",
                film.getGenre(),
                film.getPublishedYear()
        );
        send(RoutingKeys.FILM_CREATED, event);
    }

    public void publishUpdated(FilmResponse film) {
        var event = new FilmEvent.Updated(
                film.getId(),
                film.getTitle(),
                film.getImdbId(),
                film.getGenre(),
                film.getPublishedYear()
        );
        send(RoutingKeys.FILM_UPDATED, event);
    }

    public void publishDeleted(Long filmId, String title) {
        var event = new FilmEvent.Deleted(filmId, title);
        send(RoutingKeys.FILM_DELETED, event);
    }

    private void send(String routingKey, FilmEvent event) {
        try {
            EventEnvelope<FilmEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
