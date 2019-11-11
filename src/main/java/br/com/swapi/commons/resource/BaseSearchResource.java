package br.com.swapi.commons.resource;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.GenericsUtils;
import br.com.swapi.commons.Util;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.service.BaseService;
import br.com.swapi.commons.type.BaseSearchTypeDTO;

public abstract class BaseSearchResource<E extends BaseEntity, P extends BaseSearchTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, P, ID, R>>
		extends BaseResponse<E> {

	private final S service;
	private final EntityPath<E> entityPath;
	private final Class<E> entityClass;

	public BaseSearchResource(S service) {
		this.service = service;
		// Obter a class da entidade usada.
		this.entityClass = GenericsUtils.getGenericsInfo(this).getType(0);
		// Obter o path do QueryDSL.
		this.entityPath = new PathBuilder<E>(this.entityClass, Util.getInstance().varName(this.entityClass));
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

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/search/{page}/{count}")
	public ResponseEntity<Page<E>> search(HttpServletRequest request, @PathVariable("page") int page,
			@PathVariable("count") int count, @RequestBody P filter) {
		try {
			return ResponseEntity.ok(getService().search(page, count, filter));
		} catch (Exception e) {
			return (ResponseEntity<Page<E>>) genericError(e);
		}
	}

	protected final S getService() {
		return service;
	}

	protected final EntityPath<E> getEntityPath() {
		return entityPath;
	}

	protected final Class<E> getEntityClass() {
		return entityClass;
	}

}
