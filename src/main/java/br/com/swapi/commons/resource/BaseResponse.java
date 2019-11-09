package br.com.swapi.commons.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ValidationException;
import br.com.swapi.commons.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseResponse<E extends BaseEntity> {

	protected final ResponseEntity<?> responseError(Exception e, HttpStatus status) {
		log.error("Resource erro", e);
		e.printStackTrace();
		Response<E> response = new Response<E>();
		response.addError(e.getMessage());
		if (e instanceof ValidationException) {
			response.setValidation(Boolean.TRUE);
		}
		return ResponseEntity.status(status == null ? HttpStatus.BAD_REQUEST : status).body(response);
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
		Response<E> response = new Response<E>();
		response.setData(entity);
		return ResponseEntity.ok(response);
	}

	protected final ResponseEntity<Response<Iterable<E>>> ok(Iterable<E> list) {
		Response<Iterable<E>> response = new Response<Iterable<E>>();
		response.setData(list);
		return ResponseEntity.ok(response);
	}
	
	protected final ResponseEntity<?> genericError(Exception e) {
		if (e instanceof ValidationException || e instanceof EntityNotFoundException) {
			return responseErrorBadRequest(e);
		} else {
			return responseErrorInternalServerError(e);
		}
	}

}
