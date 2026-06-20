package edu.rutmiit.demo.grpcenrichment.publisher;

import edu.rutmiit.demo.events.FilmEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EnrichmentEventPublisher.class);
    private static final String SOURCE = "grpc-enrichment-client";

    private final RabbitTemplate rabbitTemplate;

    public EnrichmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

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
