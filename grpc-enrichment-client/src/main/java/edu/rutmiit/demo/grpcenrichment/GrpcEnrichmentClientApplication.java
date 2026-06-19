package edu.rutmiit.demo.grpcenrichment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * gRPC Enrichment Client — микросервис обогащения фильмов.
 *
 * Слушает событие film.created из RabbitMQ, вызывает gRPC-сервер
 * для аналитики и публикует film.enriched обратно в шину.
 *
 * Запуск:
 *   mvnw spring-boot:run -pl grpc-enrichment-client
 */
@SpringBootApplication
public class GrpcEnrichmentClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcEnrichmentClientApplication.class, args);
    }
}
