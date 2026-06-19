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

/**
 * DataFetcher для операций с режиссёрами.
 *
 * Обрабатывает корневые поля Query и Mutation, связанные с режиссёрами.
 * Вложенные поля (Director.films) обрабатываются в DirectorFilmsDataFetcher.
 *
 * Принцип разделения: один DataFetcher — одна группа связанных операций.
 * Это делает код более читаемым и тестируемым.
 */
@DgsComponent
public class DirectorDataFetcher {

    private final DirectorService directorService;

    public DirectorDataFetcher(DirectorService directorService) {
        this.directorService = directorService;
    }

    /**
     * Получение режиссёра по идентификатору.
     * Соответствует полю Query.director(id: ID!) в схеме.
     */
    @DgsQuery
    public DirectorResponse director(@InputArgument String id) {
        return directorService.findById(Long.parseLong(id));
    }

    /**
     * Список режиссёров с пагинацией.
     * Соответствует полю Query.directors(page, size) в схеме.
     */
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

    /**
     * Создание режиссёра.
     * Соответствует полю Mutation.createDirector(input) в схеме.
     */
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

    /**
     * Обновление режиссёра.
     * Соответствует полю Mutation.updateDirector(id, input) в схеме.
     */
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

    /**
     * Удаление режиссёра и всех его фильмов (каскадно).
     * Соответствует полю Mutation.deleteDirector(id) в схеме.
     */
    @DgsMutation
    public boolean deleteDirector(@InputArgument String id) {
        directorService.delete(Long.parseLong(id));
        return true;
    }
}
