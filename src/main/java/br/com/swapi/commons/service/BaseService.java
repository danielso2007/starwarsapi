package br.com.swapi.commons.service;

import java.io.Serializable;

import javax.validation.ValidationException;

import br.com.swapi.commons.type.BaseTypeDTO;
import org.springframework.data.domain.Page;

import com.querydsl.core.types.EntityPath;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.commons.type.BaseSearchTypeDTO;

public interface BaseService<E extends BaseEntity, P extends BaseSearchTypeDTO, T extends BaseTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>> {

	E findById(ID id) throws EntityNotFoundException, ValidationException;

	E save(E entity) throws ServiceException, ValidationException;

	E update(E entity, ID id) throws EntityNotFoundException, ValidationException, ServiceException;

	T getById(ID id) throws EntityNotFoundException, ValidationException;

	T save(T entity) throws ServiceException, ValidationException;

	T update(T entity, ID id) throws EntityNotFoundException, ValidationException, ServiceException;

	void delete(ID id) throws EntityNotFoundException, ServiceException;

	Iterable<T> getAll();

	Page<T> search(int page, int count, P filter);

	R getRepository();

	EntityPath<E> getEntityPath();

	Class<E> getEntityClass();

}
