package edu.rutmiit.demo.filmsapicontract.exception;

public class ImdbIdAlreadyExistsException extends RuntimeException {
    public ImdbIdAlreadyExistsException(String imdbId) {
        super("Film with IMDb ID=" + imdbId + " already exists");
    }
}
