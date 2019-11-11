package br.com.swapi.commons.resource;

import java.io.Serializable;

import javax.validation.Valid;

import br.com.swapi.commons.type.BaseTypeDTO;
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

public abstract class BaseCrudResource<E extends BaseEntity, P extends BaseSearchTypeDTO, T extends BaseTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, P, T, ID, R>>
		extends BaseSearchResource<E, P, T, ID, R, S> {

	public BaseCrudResource(S service) {
		super(service);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<T>> byId(@PathVariable("id") ID id) {
		try {
			return ok(getService().getById(id));
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping()
	public ResponseEntity<Response<T>> save(@Valid @RequestBody T objDTO, BindingResult result) {
		if (result.hasErrors()) {
			return checkErrors(result);
		}
		try {
			return ok(getService().save(objDTO));
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<T>> update(@RequestBody T objDTO, @PathVariable ID id) {
		try {
			return ok(getService().update(objDTO, id));
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<T>> delete(@PathVariable("id") ID id) {
		try {
			getService().delete(id);
			return ok();
		} catch (Exception e) {
			return (ResponseEntity<Response<T>>) genericError(e);
		}
	}

}
