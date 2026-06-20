package edu.rutmiit.demo.events;

public sealed interface DirectorEvent {

    record Created(
            Long directorId,
            String firstName,
            String lastName,
            String fullName,
            String nationality
    ) implements DirectorEvent {}

    record Deleted(
            Long directorId,
            String fullName,
            int deletedFilmsCount
    ) implements DirectorEvent {}
}
