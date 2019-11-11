package br.com.swapi.commons.resource;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import br.com.swapi.commons.type.BaseTypeDTO;
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

public abstract class BaseSearchResource<E extends BaseEntity, P extends BaseSearchTypeDTO, T extends BaseTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, P, T, ID, R>>
		extends BaseResponse<T> {

	private final S service;

	public BaseSearchResource(S service) {
		this.service = service;
	}

	@SuppressWarnings("unchecked")
	@GetMapping
	public ResponseEntity<Response<Iterable<T>>> all() {
		try {
			return ok(getService().getAll());
		} catch (Exception e) {
			return (ResponseEntity<Response<Iterable<T>>>) genericError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/search/{page}/{count}")
	public ResponseEntity<Page<T>> search(HttpServletRequest request, @PathVariable("page") int page,
			@PathVariable("count") int count, @RequestBody P filter) {
		try {
			return ResponseEntity.ok(getService().search(page, count, filter));
		} catch (Exception e) {
			return (ResponseEntity<Page<T>>) genericError(e);
		}
	}

	protected final S getService() {
		return service;
	}

}
