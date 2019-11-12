package br.com.swapi.commons.resource;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import br.com.swapi.commons.type.BaseTypeDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.GenericsUtils;
import br.com.swapi.commons.Util;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.service.BaseService;
import br.com.swapi.commons.type.BaseSearchTypeDTO;

/**
 * Recurso básico com endpoints de pesquisa.
 * @param <E> Representa a entidade.
 * @param <P> Representa o DTO de pesquisa.
 * @param <T> Representa o DTO.
 * @param <ID> O tipo do identificador.
 * @param <R> O repositorio da entidade.
 * @param <S> O serviço da entidade.
 */
public abstract class BaseSearchResource<E extends BaseEntity, P extends BaseSearchTypeDTO, T extends BaseTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, P, T, ID, R>>
        extends BaseResponse<T> {

    private final S service;

    public BaseSearchResource(S service) {
        this.service = service;
    }

    /**
     * Retorna todos os registros da base de dados.
     * @return Os registros da base de dados.
     */
    @SuppressWarnings("unchecked")
    @GetMapping(produces = "application/json")
    @ApiOperation(value = "Obter todos os registros", notes = "Cuidado, pois esta consulta pode ser custosa para a aplicação.")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Planetas listados com sucesso"),
            @ApiResponse(code = 400, message = "Erro na obtenção dos dados"),
            @ApiResponse(code = 401, message = "Você não tem permissão para acessar esse recurso"),
            @ApiResponse(code = 403, message = "É proibido acessar o recurso"),
            @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")})
    public ResponseEntity<Response<Iterable<T>>> all() {
        try {
            return ok(getService().getAll());
        } catch (Exception e) {
            return (ResponseEntity<Response<Iterable<T>>>) genericError(e);
        }
    }

    /**
     * Pesquisa por registros na base de dados.
     * @param page A página pesquisada
     * @param size Quantidade de registros por página
     * @param filter O filtro de pesquisa
     * @return A lista de registros filtrada na base de dados.
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/search/{page}/{size}", produces = "application/json", consumes = "application/json")
    @ApiOperation(value = "Pesquisa paginada de registros através de filtro.", notes = "Para este recurso, deve ser informada a página e a quantidades de registros por páginas.  ")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registros listados com sucesso"),
            @ApiResponse(code = 400, message = "Erro na obtenção dos dados")})
    public ResponseEntity<Page<T>> search(
                                          @ApiParam("A página pesquisada. Maior que zero e não pode ser vazio.") @PathVariable("page") int page,
                                          @ApiParam("A quantidade de registros por página. Não pode ser vazio.") @PathVariable("size") int size,
                                          @ApiParam("O filtro de pesquisa.") @RequestBody P filter) {
        try {
            return ResponseEntity.ok(getService().search(page, size, filter));
        } catch (Exception e) {
            return (ResponseEntity<Page<T>>) genericError(e);
        }
    }

    protected final S getService() {
        return service;
    }

}
