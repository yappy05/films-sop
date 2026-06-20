package edu.rutmiit.demo.restservice.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;
import edu.rutmiit.demo.filmsapicontract.dto.FilmResponse;
import edu.rutmiit.demo.restservice.service.DirectorService;

@DgsComponent
public class FilmDirectorDataFetcher {

    private final DirectorService directorService;

    public FilmDirectorDataFetcher(DirectorService directorService) {
        this.directorService = directorService;
    }

    @DgsData(parentType = "Film", field = "director")
    public DirectorResponse director(DgsDataFetchingEnvironment dfe) {
        FilmResponse film = dfe.getSource();

        if (film.getDirector() != null) {
            return film.getDirector();
        }

        return null;
    }
}
