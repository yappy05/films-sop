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

/**
 * Публикация доменных событий фильмов в RabbitMQ.
 *
 * Паттерн: FilmService вызывает publish-метод ПОСЛЕ успешного завершения
 * бизнес-операции. Если RabbitMQ недоступен — событие логируется как ошибка,
 * но основная операция (создание/удаление фильма) НЕ откатывается.
 *
 * Это паттерн «fire-and-forget» — допустимая потеря события лучше,
 * чем отказ бизнес-операции из-за недоступности брокера.
 *
 * В промышленных системах для гарантированной доставки используют:
 * - Transactional Outbox (запись события в БД в одной транзакции с данными),
 * - Change Data Capture (Debezium/Kafka Connect).
 */
@Component
public class FilmEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(FilmEventPublisher.class);
    private static final String SOURCE = "rest-service";

    private final RabbitTemplate rabbitTemplate;

    public FilmEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует событие «фильм создана».
     */
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

    /**
     * Публикует событие «фильм обновлена».
     */
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

    /**
     * Публикует событие «фильм удалена».
     */
    public void publishDeleted(Long filmId, String title) {
        var event = new FilmEvent.Deleted(filmId, title);
        send(RoutingKeys.FILM_DELETED, event);
    }

    /**
     * Отправляет событие в RabbitMQ, обёрнутое в EventEnvelope.
     *
     * convertAndSend:
     * - 1й аргумент: имя exchange
     * - 2й аргумент: routing key (определяет, в какие очереди попадёт сообщение)
     * - 3й аргумент: объект, который Jackson сериализует в JSON
     *
     * try-catch гарантирует, что ошибка публикации не сломает основной бизнес-поток.
     */
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
