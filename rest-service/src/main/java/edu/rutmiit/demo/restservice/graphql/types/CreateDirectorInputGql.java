package edu.rutmiit.demo.restservice.graphql.types;

import java.time.LocalDate;

/**
 * Входной тип для создания режиссёра.
 * Соответствует input CreateDirectorInput в GraphQL-схеме.
 */
public record CreateDirectorInputGql(
        String firstName,
        String lastName,
        String email,
        String bio,
        LocalDate birthDate,
        String nationality
) {}
