package br.com.swapi.commons.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ValidationException;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.response.Response.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseResponse<E extends BaseEntity> {

	protected final ResponseEntity<?> responseError(Exception e, HttpStatus status) {
		log.error("Resource erro", e);
		List<String> listError = new ArrayList<String>();
		listError.add(e.getMessage());

		ResponseBuilder<E> responseBuilder = Response.<E>builder();
		responseBuilder.errors(listError);

		if (e instanceof ValidationException) {
			responseBuilder.validation(Boolean.TRUE);
		}

		return ResponseEntity.status(status == null ? HttpStatus.BAD_REQUEST : status).body(responseBuilder.build());
	}

	protected final ResponseEntity<?> responseErrorBadRequest(Exception e) {
		return responseError(e, HttpStatus.BAD_REQUEST);
	}

	protected final ResponseEntity<?> responseErrorInternalServerError(Exception e) {
		return responseError(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected final ResponseEntity<Response<E>> ok() {
		return ResponseEntity.ok().build();
	}

	protected final ResponseEntity<Response<E>> ok(E entity) {
		return ResponseEntity.ok(Response.<E>builder().data(entity).build());
	}

	protected final ResponseEntity<Response<Iterable<E>>> ok(Iterable<E> list) {
		return ResponseEntity.ok(Response.<Iterable<E>>builder().data(list).build());
	}

	protected final ResponseEntity<?> genericError(Exception e) {
		if (e instanceof ValidationException || e instanceof EntityNotFoundException) {
			return responseErrorBadRequest(e);
		} else {
			return responseErrorInternalServerError(e);
		}
	}

	protected ResponseEntity<Response<E>> checkErrors(BindingResult result) {
		Response<E> response = Response.<E>builder().build();
		result.getAllErrors().forEach(error -> response.addError(error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(response);
	}

}
