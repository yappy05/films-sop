package edu.rutmiit.demo.restservice.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.filmsapicontract.dto.DirectorRequest;
import edu.rutmiit.demo.filmsapicontract.dto.DirectorResponse;
import edu.rutmiit.demo.filmsapicontract.dto.PagedResponse;
import edu.rutmiit.demo.restservice.graphql.types.DirectorConnectionGql;
import edu.rutmiit.demo.restservice.graphql.types.CreateDirectorInputGql;
import edu.rutmiit.demo.restservice.graphql.types.PageInfoGql;
import edu.rutmiit.demo.restservice.graphql.types.UpdateDirectorInputGql;
import edu.rutmiit.demo.restservice.service.DirectorService;

@DgsComponent
public class DirectorDataFetcher {

    private final DirectorService directorService;

    public DirectorDataFetcher(DirectorService directorService) {
        this.directorService = directorService;
    }

    @DgsQuery
    public DirectorResponse director(@InputArgument String id) {
        return directorService.findById(Long.parseLong(id));
    }

    @DgsQuery
    public DirectorConnectionGql directors(
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PagedResponse<DirectorResponse> paged = directorService.findAll(pageNum, pageSize);

        return new DirectorConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }

    @DgsMutation
    public DirectorResponse createDirector(@InputArgument CreateDirectorInputGql input) {
        DirectorRequest request = new DirectorRequest(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.bio(),
                input.birthDate(),
                input.nationality()
        );
        return directorService.create(request);
    }

    @DgsMutation
    public DirectorResponse updateDirector(@InputArgument String id, @InputArgument UpdateDirectorInputGql input) {
        DirectorRequest request = new DirectorRequest(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.bio(),
                input.birthDate(),
                input.nationality()
        );
        return directorService.update(Long.parseLong(id), request);
    }

    @DgsMutation
    public boolean deleteDirector(@InputArgument String id) {
        directorService.delete(Long.parseLong(id));
        return true;
    }
}
