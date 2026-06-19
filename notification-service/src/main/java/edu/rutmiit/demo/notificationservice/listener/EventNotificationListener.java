package edu.rutmiit.demo.notificationservice.listener;

import edu.rutmiit.demo.events.*;
import edu.rutmiit.demo.notificationservice.websocket.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Слушатель всех доменных событий из RabbitMQ.
 *
 * Получает события из очереди q.notifications.all (binding "#"),
 * формирует человекочитаемое JSON-уведомление и рассылает
 * всем подключённым WebSocket-клиентам через NotificationWebSocketHandler.
 *
 * Дедупликация — по eventId (на случай повторной доставки RabbitMQ).
 */
@Component
public class EventNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationListener.class);

    private final NotificationWebSocketHandler webSocketHandler;
    private final JsonMapper jsonMapper;

    /** Набор обработанных eventId для дедупликации. */
    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();

    public EventNotificationListener(NotificationWebSocketHandler webSocketHandler,
                                     JsonMapper jsonMapper) {
        this.webSocketHandler = webSocketHandler;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.notifications.all", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            // Парсим метаданные
            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            // Дедупликация по eventId 
            if (!processedEventIds.add(metadata.eventId())) {
                log.warn("Дубликат уведомления пропущен: eventId={}", metadata.eventId());
                return;
            }

            // Формируем уведомление
            JsonNode payloadNode = root.get("payload");
            String title = buildTitle(metadata.eventType());
            String description = buildDescription(metadata.eventType(), payloadNode);
            String icon = resolveIcon(metadata.eventType());
            String level = resolveLevel(metadata.eventType());

            // JSON для WebSocket-клиента 
            String notificationJson = jsonMapper.writeValueAsString(
                    new NotificationPayload(
                            "NOTIFICATION",
                            metadata.eventId(),
                            metadata.eventType(),
                            title,
                            description,
                            icon,
                            level,
                            metadata.source(),
                            metadata.timestamp().toString(),
                            Instant.now().toString()
                    )
            );

            // Broadcast в WebSocket
            webSocketHandler.broadcast(notificationJson);

            log.info("[NOTIFY] {} | {} (клиентов: {})",
                    metadata.eventType(), description, webSocketHandler.getActiveConnectionCount());

        } catch (Exception e) {
            log.error("Ошибка обработки события для уведомлений: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }

    // Формирование заголовка уведомления

    private String buildTitle(String eventType) {
        return switch (eventType) {
            case "film.created"   -> "Новый фильм";
            case "film.updated"   -> "Фильм обновлён";
            case "film.deleted"   -> "Фильм удалён";
            case "film.enriched"  -> "Аналитика фильма";
            case "director.created" -> "Новый режиссёр";
            case "director.deleted" -> "Режиссёр удалён";
            default               -> "Событие: " + eventType;
        };
    }

    // Формирование описания

    private String buildDescription(String eventType, JsonNode payload) {
        try {
            return switch (eventType) {
                case "film.created" -> {
                    FilmEvent.Created e = jsonMapper.treeToValue(payload, FilmEvent.Created.class);
                    yield "Создан фильм «%s» (IMDb ID: %s), режиссёр: %s".formatted(
                            e.title(), e.imdbId(), e.directorFullName());
                }
                case "film.updated" -> {
                    FilmEvent.Updated e = jsonMapper.treeToValue(payload, FilmEvent.Updated.class);
                    yield "Обновлён фильм id=%d «%s»".formatted(e.filmId(), e.title());
                }
                case "film.deleted" -> {
                    FilmEvent.Deleted e = jsonMapper.treeToValue(payload, FilmEvent.Deleted.class);
                    yield "Удалён фильм id=%d «%s»".formatted(e.filmId(), e.title());
                }
                case "film.enriched" -> {
                    FilmEvent.Enriched e = jsonMapper.treeToValue(payload, FilmEvent.Enriched.class);
                    yield "Фильм «%s» — чтение: %dмин, сложность: %s, балл: %.1f, эпоха: %s".formatted(
                            e.title(), e.estimatedReadingMinutes(),
                            e.difficultyLevel(), e.recommendationScore(), e.eraClassification());
                }
                case "director.created" -> {
                    DirectorEvent.Created e = jsonMapper.treeToValue(payload, DirectorEvent.Created.class);
                    yield "Создан режиссёр «%s» (национальность: %s)".formatted(
                            e.fullName(), e.nationality());
                }
                case "director.deleted" -> {
                    DirectorEvent.Deleted e = jsonMapper.treeToValue(payload, DirectorEvent.Deleted.class);
                    yield "Удалён режиссёр «%s» (удалено фильмов: %d)".formatted(
                            e.fullName(), e.deletedFilmsCount());
                }
                default -> "Неизвестное событие: " + eventType;
            };
        } catch (Exception e) {
            return "Событие " + eventType + " (ошибка парсинга)";
        }
    }

    // Иконка по типу события

    private String resolveIcon(String eventType) {
        return switch (eventType) {
            case "film.created"   -> "film-plus";
            case "film.updated"   -> "film-edit";
            case "film.deleted"   -> "film-remove";
            case "film.enriched"  -> "analytics";
            case "director.created" -> "director-plus";
            case "director.deleted" -> "director-remove";
            default               -> "bell";
        };
    }

    // Уровень уведомления

    private String resolveLevel(String eventType) {
        return switch (eventType) {
            case "film.deleted", "director.deleted" -> "warning";
            case "film.enriched"                  -> "info";
            default                               -> "success";
        };
    }

    /**
     * Payload уведомления для WebSocket.
     */
    record NotificationPayload(
            String type,
            String eventId,
            String eventType,
            String title,
            String description,
            String icon,
            String level,
            String source,
            String eventTimestamp,
            String receivedAt
    ) {}
}
