package br.com.swapi.commons.resource;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.GenericsInfo;
import br.com.swapi.commons.GenericsUtils;
import br.com.swapi.commons.Util;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.service.GenericService;

public abstract class BaseCrudResource<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>, S extends GenericService<E, ID, R>>
		extends BaseSearchResource<E, ID, R, S> {

	private S service;

	protected final EntityPath<E> entityPath;
	private Class<E> entityClass;

	public BaseCrudResource(S service) {
		super(service);
		this.service = service;
		GenericsInfo genericsInfo = GenericsUtils.getGenericsInfo(this);
		this.entityClass = genericsInfo.getType(0);
		this.entityPath = new PathBuilder<E>(this.entityClass, Util.varName(this.entityClass));
	}

	protected S getService() {
		return this.service;
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
	public ResponseEntity<Response<E>> save(@Valid @RequestBody E objDTO) {
		try {
			return ok((E) getService().save(objDTO));
		} catch (Exception e) {
			return (ResponseEntity<Response<E>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<E>> update(@Valid @RequestBody E objDTO, @PathVariable ID id) {
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
