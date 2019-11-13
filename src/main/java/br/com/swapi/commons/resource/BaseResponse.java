package br.com.swapi.commons.resource;

import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ValidationException;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.response.Response.ResponseBuilder;
import br.com.swapi.commons.type.BaseTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsável pelos retornos dos recursos.
 * @param <T> O DTO.
 */
@Slf4j
public abstract class BaseResponse<T extends BaseTypeDTO> {

	/**
	 * Retorna um {@link ResponseEntity} com a exceção e o {@link HttpStatus} definidos.
	 * @param e A exceção como error.
	 * @param status O status de retorno.
	 * @return Retorna um {@link ResponseEntity}.
	 */
	protected final ResponseEntity<?> responseError(Exception e, HttpStatus status) {
		log.error("Resource erro", e);
		List<String> listError = new ArrayList<>();
		listError.add(e.getMessage());

		ResponseBuilder<T> responseBuilder = Response.builder();
		responseBuilder.errors(listError);

		if (e instanceof ValidationException) {
			responseBuilder.validation(Boolean.TRUE);
		}

		return ResponseEntity.status(status == null ? HttpStatus.BAD_REQUEST : status).body(responseBuilder.build());
	}

	/**
	 * Retorna um {@link ResponseEntity} com status 400 com a exceção definida.
	 * @param e A exceção para o erro.
	 * @return Retorna um {@link ResponseEntity}.
	 */
	protected final ResponseEntity<?> responseErrorBadRequest(Exception e) {
		return responseError(e, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Retorna um {@link ResponseEntity} com status 50 com a exceção definida.
	 * @param e A exceção para o erro.
	 * @return Retorna um {@link ResponseEntity}.
	 */
	protected final ResponseEntity<?> responseErrorInternalServerError(Exception e) {
		return responseError(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Retorna um {@link ResponseEntity} com status 200.
	 * @return Retorna um {@link ResponseEntity}
	 */
	protected final ResponseEntity<Response<T>> ok() {
		return ResponseEntity.ok().build();
	}

	/**
	 * Retorna um {@link ResponseEntity} com status 200.
	 * @param entity O dado que sera enviado no {@link ResponseEntity}.
	 * @return {@link ResponseEntity}
	 */
	protected final ResponseEntity<Response<T>> ok(T entity) {
		return ResponseEntity.ok(Response.<T>builder().data(entity).build());
	}

	/**
	 * Retorna um {@link ResponseEntity} com status 200.
	 * @param list O dado que sera enviado no {@link ResponseEntity}.
	 * @return {@link ResponseEntity}
	 */
	protected final ResponseEntity<Response<List<T>>> ok(List<T> list) {
		return ResponseEntity.ok(Response.<List<T>>builder().data(list).build());
	}

	/**
	 * Retorna um {@link ResponseEntity} com status 400 ou status 500
	 * dependendo da exceção {@link ValidationException} ou {@link EntityNotFoundException}.
	 * @param e A {@link Exception}
	 * @return {@link ResponseEntity}
	 */
	protected final ResponseEntity<?> genericError(Exception e) {
		if (e instanceof ValidationException || e instanceof EntityNotFoundException) {
			return responseErrorBadRequest(e);
		} else {
			return responseErrorInternalServerError(e);
		}
	}

	/**
	 * Obtem os erros de um {@link BindingResult}.
	 * @param result O {@link BindingResult}
	 * @return O {@link ResponseEntity} com os erros.
	 */
	protected ResponseEntity<Response<T>> checkErrors(BindingResult result) {
		Response<T> response = Response.<T>builder().build();
		result.getAllErrors().forEach(error -> response.addError(error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(response);
	}

}
