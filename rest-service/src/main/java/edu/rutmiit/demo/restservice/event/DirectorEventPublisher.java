package edu.rutmiit.demo.restservice.event;

import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;
import edu.rutmiit.demo.events.DirectorEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DirectorEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DirectorEventPublisher.class);
    private static final String SOURCE = "rest-service";

    private final RabbitTemplate rabbitTemplate;

    public DirectorEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(DirectorResponse director) {
        var event = new DirectorEvent.Created(
                director.getId(),
                director.getFirstName(),
                director.getLastName(),
                director.getFullName(),
                director.getNationality()
        );
        send(RoutingKeys.DIRECTOR_CREATED, event);
    }

    public void publishDeleted(DirectorResponse director, int deletedFilmsCount) {
        var event = new DirectorEvent.Deleted(
                director.getId(),
                director.getFullName(),
                deletedFilmsCount
        );
        send(RoutingKeys.DIRECTOR_DELETED, event);
    }

    private void send(String routingKey, DirectorEvent event) {
        try {
            EventEnvelope<DirectorEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
