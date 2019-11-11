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

@Slf4j
public abstract class BaseResponse<T extends BaseTypeDTO> {

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

	protected final ResponseEntity<?> responseErrorBadRequest(Exception e) {
		return responseError(e, HttpStatus.BAD_REQUEST);
	}

	protected final ResponseEntity<?> responseErrorInternalServerError(Exception e) {
		return responseError(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected final ResponseEntity<Response<T>> ok() {
		return ResponseEntity.ok().build();
	}

	protected final ResponseEntity<Response<T>> ok(T entity) {
		return ResponseEntity.ok(Response.<T>builder().data(entity).build());
	}

	protected final ResponseEntity<Response<Iterable<T>>> ok(Iterable<T> list) {
		return ResponseEntity.ok(Response.<Iterable<T>>builder().data(list).build());
	}

	protected final ResponseEntity<?> genericError(Exception e) {
		if (e instanceof ValidationException || e instanceof EntityNotFoundException) {
			return responseErrorBadRequest(e);
		} else {
			return responseErrorInternalServerError(e);
		}
	}

	protected ResponseEntity<Response<T>> checkErrors(BindingResult result) {
		Response<T> response = Response.<T>builder().build();
		result.getAllErrors().forEach(error -> response.addError(error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(response);
	}

}
