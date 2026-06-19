package edu.rutmiit.demo.grpcenrichment.publisher;

import edu.rutmiit.demo.events.FilmEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Публикация событий обогащения (film.enriched) в RabbitMQ.
 *
 * Аналогичен FilmEventPublisher в rest-service, но публикует другой тип события.
 * Паттерн fire-and-forget: если RabbitMQ недоступен, ошибка логируется,
 * но gRPC-вызов уже выполнен — результат не теряется полностью.
 */
@Component
public class EnrichmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EnrichmentEventPublisher.class);
    private static final String SOURCE = "grpc-enrichment-client";

    private final RabbitTemplate rabbitTemplate;

    public EnrichmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует событие film.enriched с результатами gRPC-аналитики.
     */
    public void publishEnriched(FilmEvent.Enriched enrichedEvent) {
        try {
            EventEnvelope<FilmEvent> envelope = EventEnvelope.wrap(
                    enrichedEvent, SOURCE, RoutingKeys.FILM_ENRICHED);

            rabbitTemplate.convertAndSend(
                    RoutingKeys.EXCHANGE,
                    RoutingKeys.FILM_ENRICHED,
                    envelope);

            log.info("Событие отправлено: {} [filmId={}, eventId={}]",
                    RoutingKeys.FILM_ENRICHED,
                    enrichedEvent.filmId(),
                    envelope.metadata().eventId());

        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}",
                    RoutingKeys.FILM_ENRICHED, e.getMessage());
        }
    }
}
