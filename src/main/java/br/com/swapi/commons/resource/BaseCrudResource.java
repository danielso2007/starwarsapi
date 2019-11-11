package br.com.swapi.commons.resource;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.service.BaseService;
import br.com.swapi.commons.type.BaseSearchTypeDTO;

public abstract class BaseCrudResource<E extends BaseEntity, P extends BaseSearchTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, P, ID, R>>
		extends BaseSearchResource<E, P, ID, R, S> {

	public BaseCrudResource(S service) {
		super(service);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<E>> byId(@PathVariable("id") ID id) {
		try {
			return ok((E) getService().getById(id));
		} catch (Exception e) {
			return (ResponseEntity<Response<E>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping()
	public ResponseEntity<Response<E>> save(@Valid @RequestBody E objDTO, BindingResult result) {
		if (result.hasErrors()) {
			return checkErrors(result);
		}
		try {
			return ok((E) getService().save(objDTO));
		} catch (Exception e) {
			return (ResponseEntity<Response<E>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<E>> update(@RequestBody E objDTO, @PathVariable ID id) {
		try {
			return ok((E) getService().update(objDTO, id));
		} catch (Exception e) {
			return (ResponseEntity<Response<E>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<E>> delete(@PathVariable("id") ID id) {
		try {
			getService().delete(id);
			return ok();
		} catch (Exception e) {
			return (ResponseEntity<Response<E>>) genericError(e);
		}
	}

}
