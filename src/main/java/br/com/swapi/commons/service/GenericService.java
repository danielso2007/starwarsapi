package br.com.swapi.commons.service;

import java.io.Serializable;

import javax.validation.ValidationException;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;

public interface GenericService<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>> {

    Class<E> getEntityClass();

    E getById(ID id) throws EntityNotFoundException, ValidationException;
    
    Iterable<E> getAll();

    E save(E entity) throws ServiceException, ValidationException;
    
    E update(E entity, ID id) throws EntityNotFoundException, ValidationException, ServiceException;

    void delete(ID id) throws EntityNotFoundException, ServiceException;

    R getRepository();

}

