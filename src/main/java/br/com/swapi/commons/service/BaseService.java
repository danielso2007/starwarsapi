package br.com.swapi.commons.service;

import java.io.Serializable;

import javax.validation.ValidationException;

import org.springframework.data.domain.Page;

import com.querydsl.core.types.EntityPath;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.commons.type.BaseSearchTypeDTO;

public interface BaseService<E extends BaseEntity, P extends BaseSearchTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>> {

	E getById(ID id) throws EntityNotFoundException, ValidationException;

	E save(E entity) throws ServiceException, ValidationException;

	E update(E entity, ID id) throws EntityNotFoundException, ValidationException, ServiceException;

	void delete(ID id) throws EntityNotFoundException, ServiceException;

	Iterable<E> getAll();

	Page<E> search(int page, int count, P filter);

	R getRepository();

	EntityPath<E> getEntityPath();

	Class<E> getEntityClass();

}
