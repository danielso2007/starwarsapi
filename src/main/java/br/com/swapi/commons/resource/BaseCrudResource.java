package br.com.swapi.commons.resource;

import java.io.Serializable;

import javax.validation.Valid;

import br.com.swapi.commons.Constants;
import br.com.swapi.commons.type.BaseTypeDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.service.BaseService;
import br.com.swapi.commons.type.BaseSearchTypeDTO;

/**
 * Recurso básico com endpoints de CRUD.
 * @param <E> Representa a entidade.
 * @param <P> Representa o DTO de pesquisa.
 * @param <T> Representa o DTO.
 * @param <ID> O tipo do identificador.
 * @param <R> O repositorio da entidade.
 * @param <S> O serviço da entidade.
 */
public abstract class BaseCrudResource<E extends BaseEntity, P extends BaseSearchTypeDTO, T extends BaseTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, P, T, ID, R>>
		extends BaseSearchResource<E, P, T, ID, R, S> {

	public BaseCrudResource(S service) {
		super(service);
	}

	/**
	 * Pesquisa um registro na base de dados.
	 * @param id O identificador do registro.
	 * @return Um registro da base de dados.
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/{id}", produces = {Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8})
	@ApiOperation(value = "Obter registro pelo identificador", notes = "Será retornado um registro da base de dados.")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registro carregado com sucesso"),
			@ApiResponse(code = 400, message = "Erro de dados ou validação")})
	public ResponseEntity<Response<T>> byId(
			@ApiParam("O identificador do registro. Não pode ser vazio.")
			@PathVariable("id") ID id) {
		try {
			return ok(getService().getById(id));
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

	/**
	 * Salvar um registro na base de dados.
	 * @param object O registro a ser gravado.
	 * @param result represents binding results.
	 * @return O registro cadastrado na base de dados.
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(produces = Constants.APPLICATION_JSON_UTF_8, consumes = Constants.APPLICATION_JSON_UTF_8)
	@ApiOperation(value = "Salvar um novo registro", notes = "Cria um novo registro na base de dados.")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Registro criado com sucesso"),
			@ApiResponse(code = 400, message = "Erro de dados ou validação")})
	public ResponseEntity<Response<T>> save(
			@ApiParam("O registro a ser criado.") @Valid @RequestBody T object,
			BindingResult result) {
		if (result.hasErrors()) {
			return checkErrors(result);
		}
		try {
			return ok(getService().save(object));
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

	/**
	 * Obter um registro na base de dados.
	 * @param object O registro a ser atualizado.
	 * @param id O identificador do registro.
	 * @return O registro atualizado.
	 */
	@SuppressWarnings("unchecked")
	@PutMapping(value = "/{id}", produces = Constants.APPLICATION_JSON_UTF_8, consumes = Constants.APPLICATION_JSON_UTF_8)
	@ApiOperation(value = "Atualizar um registro", notes = "Atualiza um registro na base de dados.")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registro atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro de dados ou validação")})
	public ResponseEntity<Response<T>> update(
			@ApiParam("O registro a ser atualizado.") @RequestBody T object,
			@ApiParam("O identificador do registro.") @PathVariable ID id) {
		try {
			return ok(getService().update(object, id));
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

	/**
	 * Remove um registro na base de dados.
	 * @param id O identificador do registro.
	 * @return HttpStatus.OK
	 */
	@SuppressWarnings("unchecked")
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value = "Deletar um registro")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registro deletado com sucesso"),
			@ApiResponse(code = 400, message = "Erro de dados ou validação")})
	public ResponseEntity<Response<T>> delete(@ApiParam("O identificador do registro.") @PathVariable("id") ID id) {
		try {
			getService().delete(id);
			return ok();
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

}
