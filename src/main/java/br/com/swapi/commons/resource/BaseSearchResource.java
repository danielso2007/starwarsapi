package br.com.swapi.commons.resource;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.GenericsInfo;
import br.com.swapi.commons.GenericsUtils;
import br.com.swapi.commons.Util;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.service.GenericService;

public abstract class BaseSearchResource<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>, S extends GenericService<E, ID, R>>
		extends BaseResponse<E> {

	private S service;

	protected final EntityPath<E> entityPath;
	private Class<E> entityClass;

	public BaseSearchResource(S service) {
		this.service = service;
		GenericsInfo genericsInfo = GenericsUtils.getGenericsInfo(this);
		this.entityClass = genericsInfo.getType(0);
		this.entityPath = new PathBuilder<E>(this.entityClass, Util.varName(this.entityClass));
	}

	protected S getService() {
		return this.service;
	}

	@SuppressWarnings("unchecked")
	@GetMapping
	public ResponseEntity<Response<Iterable<E>>> all() {
		try {
			return ok(getService().getAll());
		} catch (Exception e) {
			return (ResponseEntity<Response<Iterable<E>>>) genericError(e);
		}
	}
	
}
