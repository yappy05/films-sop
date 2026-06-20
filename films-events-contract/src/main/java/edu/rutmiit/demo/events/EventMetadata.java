package edu.rutmiit.demo.events;

import java.time.Instant;
import java.util.UUID;

public record EventMetadata(

        String eventId,

        Instant timestamp,

        String source,

        String eventType
) {

    public static EventMetadata create(String source, String eventType) {
        return new EventMetadata(
                UUID.randomUUID().toString(),
                Instant.now(),
                source,
                eventType
        );
    }
}
