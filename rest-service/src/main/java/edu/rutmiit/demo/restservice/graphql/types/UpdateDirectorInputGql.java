package edu.rutmiit.demo.restservice.graphql.types;

import java.time.LocalDate;

/**
 * Входной тип для обновления режиссёра.
 * Соответствует input UpdateDirectorInput в GraphQL-схеме.
 */
public record UpdateDirectorInputGql(
        String firstName,
        String lastName,
        String email,
        String bio,
        LocalDate birthDate,
        String nationality
) {}
