package edu.rutmiit.demo.grpcenrichment.listener;

import edu.rutmiit.demo.events.FilmEvent;
import edu.rutmiit.demo.events.EventMetadata;
import edu.rutmiit.demo.grpc.AnalyzeFilmRequest;
import edu.rutmiit.demo.grpc.FilmAnalysisResponse;
import edu.rutmiit.demo.grpc.FilmAnalyticsGrpc;
import edu.rutmiit.demo.grpcenrichment.publisher.EnrichmentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Слушатель событий film.created из RabbitMQ.
 *
 * Десериализация — ручная (как в audit-service), потому что EventEnvelope<T>
 * является generic-типом, и Jackson не может определить конкретный подтип T.
 */
@Component
public class FilmCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(FilmCreatedListener.class);

    private final FilmAnalyticsGrpc.FilmAnalyticsBlockingStub analyticsStub;
    private final EnrichmentEventPublisher enrichmentPublisher;
    private final JsonMapper jsonMapper;

    public FilmCreatedListener(FilmAnalyticsGrpc.FilmAnalyticsBlockingStub analyticsStub,
                               EnrichmentEventPublisher enrichmentPublisher,
                               JsonMapper jsonMapper) {
        this.analyticsStub = analyticsStub;
        this.enrichmentPublisher = enrichmentPublisher;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Обрабатывает событие film.created:
     * 1. Десериализует событие из JSON
     * 2. Формирует gRPC-запрос
     * 3. Вызывает gRPC-сервер (синхронно)
     * 4. Публикует результат как событие film.enriched
     */
    @RabbitListener(queues = "q.enrichment.film-created", messageConverter = "")
    public void handleFilmCreated(Message message) {
        try {
            // 1. Парсим JSON-конверт
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            FilmEvent.Created filmCreated = jsonMapper.treeToValue(payloadNode, FilmEvent.Created.class);

            log.info("Получено событие film.created: filmId={}, «{}» [eventId={}]",
                    filmCreated.filmId(), filmCreated.title(), metadata.eventId());

            // 2. Формируем gRPC-запрос
            AnalyzeFilmRequest grpcRequest = AnalyzeFilmRequest.newBuilder()
                    .setFilmId(filmCreated.filmId())
                    .setTitle(filmCreated.title())
                    .setGenre(filmCreated.genre() != null ? filmCreated.genre() : "")
                    .setPublishedYear(filmCreated.publishedYear() != null ? filmCreated.publishedYear() : 0)
                    .setDirectorName(filmCreated.directorFullName() != null ? filmCreated.directorFullName() : "")
                    .build();

            // 3. Вызываем gRPC-сервер (синхронно)
            log.info("Вызов gRPC: FilmAnalytics.AnalyzeFilm(filmId={})", filmCreated.filmId());
            FilmAnalysisResponse grpcResponse = analyticsStub.analyzeFilm(grpcRequest);

            log.info("gRPC ответ получен: filmId={}, время={}мин, сложность={}, балл={}, эпоха={}",
                    grpcResponse.getFilmId(),
                    grpcResponse.getEstimatedReadingMinutes(),
                    grpcResponse.getDifficultyLevel(),
                    grpcResponse.getRecommendationScore(),
                    grpcResponse.getEraClassification());

            // 4. Публикуем событие film.enriched
            FilmEvent.Enriched enrichedEvent = new FilmEvent.Enriched(
                    grpcResponse.getFilmId(),
                    filmCreated.title(),
                    grpcResponse.getEstimatedReadingMinutes(),
                    grpcResponse.getDifficultyLevel(),
                    grpcResponse.getRecommendationScore(),
                    grpcResponse.getEraClassification()
            );

            enrichmentPublisher.publishEnriched(enrichedEvent);

            log.info("Фильм обогащён: filmId={}, «{}» → film.enriched отправлено",
                    filmCreated.filmId(), filmCreated.title());

        } catch (io.grpc.StatusRuntimeException e) {
            log.error("gRPC ошибка при обогащении фильма: {} ({})",
                    e.getStatus().getDescription(), e.getStatus().getCode());
            throw new RuntimeException("gRPC-вызов завершился ошибкой", e);

        } catch (Exception e) {
            log.error("Ошибка обработки события film.created: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие film.created", e);
        }
    }
}
