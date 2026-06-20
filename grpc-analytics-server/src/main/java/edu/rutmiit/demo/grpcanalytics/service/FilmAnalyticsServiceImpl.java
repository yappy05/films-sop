package edu.rutmiit.demo.grpcanalytics.service;

import edu.rutmiit.demo.grpc.AnalyzeFilmRequest;
import edu.rutmiit.demo.grpc.FilmAnalysisResponse;
import edu.rutmiit.demo.grpc.FilmAnalyticsGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilmAnalyticsServiceImpl extends FilmAnalyticsGrpc.FilmAnalyticsImplBase {

    private static final Logger log = LoggerFactory.getLogger(FilmAnalyticsServiceImpl.class);

    @Override
    public void analyzeFilm(AnalyzeFilmRequest request,
                            StreamObserver<FilmAnalysisResponse> responseObserver) {

        log.info("gRPC запрос: анализ фильма id={} «{}» (жанр: {}, год: {})",
                request.getFilmId(), request.getTitle(),
                request.getGenre(), request.getPublishedYear());

        int readingMinutes = estimateReadingTime(request.getGenre(), request.getPublishedYear());
        String difficulty = classifyDifficulty(request.getGenre(), request.getPublishedYear());
        double score = calculateRecommendationScore(request.getGenre(), request.getPublishedYear());
        String era = classifyEra(request.getPublishedYear());

        FilmAnalysisResponse response = FilmAnalysisResponse.newBuilder()
                .setFilmId(request.getFilmId())
                .setEstimatedReadingMinutes(readingMinutes)
                .setDifficultyLevel(difficulty)
                .setRecommendationScore(score)
                .setEraClassification(era)
                .build();

        log.info("gRPC ответ: фильм id={}, время чтения={}мин, сложность={}, балл={}, эпоха={}",
                response.getFilmId(), readingMinutes, difficulty, score, era);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private int estimateReadingTime(String genre, int publishedYear) {
        int base = switch (genre != null ? genre.toLowerCase() : "") {
            case "роман", "novel"       -> 720;
            case "поэма", "poem"        -> 180;
            case "пьеса", "drama"       -> 240;
            case "рассказ", "story"     -> 60;
            case "эпопея", "epic"       -> 1440;
            default                     -> 360;
        };

        if (publishedYear > 0 && publishedYear < 1900) {
            base = (int) (base * 1.3);
        }
        return base;
    }

    private String classifyDifficulty(String genre, int publishedYear) {
        if (publishedYear > 0 && publishedYear < 1800) return "CLASSIC";
        if (publishedYear >= 1800 && publishedYear < 1950) return "HARD";
        if (publishedYear >= 1950 && publishedYear < 2000) return "MEDIUM";
        return "EASY";
    }

    private double calculateRecommendationScore(String genre, int publishedYear) {
        double base = 7.0;
        if (publishedYear > 0 && publishedYear < 1900) base += 1.5;
        if ("роман".equalsIgnoreCase(genre) || "novel".equalsIgnoreCase(genre)) base += 0.5;
        if ("эпопея".equalsIgnoreCase(genre) || "epic".equalsIgnoreCase(genre)) base += 1.0;
        return Math.min(base, 10.0);
    }

    private String classifyEra(int publishedYear) {
        if (publishedYear <= 0) return "UNKNOWN";
        if (publishedYear < 1600) return "ANCIENT";
        if (publishedYear < 1900) return "CLASSICAL";
        if (publishedYear < 2000) return "MODERN";
        return "CONTEMPORARY";
    }
}
