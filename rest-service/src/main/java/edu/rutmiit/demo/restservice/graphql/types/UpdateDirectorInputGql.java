package edu.rutmiit.demo.restservice.graphql.types;

import java.time.LocalDate;

public record UpdateDirectorInputGql(
        String firstName,
        String lastName,
        String email,
        String bio,
        LocalDate birthDate,
        String nationality
) {}
