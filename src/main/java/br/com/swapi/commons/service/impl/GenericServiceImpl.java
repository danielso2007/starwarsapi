package br.com.swapi.commons.service.impl;

import java.beans.Transient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.GenericsInfo;
import br.com.swapi.commons.GenericsUtils;
import br.com.swapi.commons.Util;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.commons.service.GenericService;

@Transactional(readOnly = true, rollbackFor = { Exception.class })
public abstract class GenericServiceImpl<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>>
        implements GenericService<E, ID, R> {

    public static final String NAO_EXISTE_REGISTRO_COM_O_ID_INFORMADO = "Não existe registro com o ID informado.";
    public static final String ID_NAO_INFORMADO = "Id não informado.";
    public static final String ENTIDADE_NAO_INFORMADA = "Entidade não informada.";
    public static final String ENTIDADE_NAO_ENCONTRADA = "Entidade não encontrada.";

    private Logger log = LoggerFactory.getLogger(GenericServiceImpl.class);

    private R repository;

    protected final EntityPath<E> entityPath;
    private Class<E> entityClass;

    public GenericServiceImpl(R repository) {
        this.repository = repository;
        GenericsInfo genericsInfo = GenericsUtils.getGenericsInfo(this);
        this.entityClass = genericsInfo.getType(1);
        this.entityPath = new PathBuilder<E>(this.entityClass, Util.varName(this.entityClass));
    }

    @Override
    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public E getById(ID id) throws EntityNotFoundException, ValidationException {
        try {
            if (id == null) {
                throw new ValidationException(ID_NAO_INFORMADO);
            }
            log.info("Obter {} pelo id {}.", entityClass.getName(), id);
            Optional<E> optional = this.repository.findById(id);

            if (!optional.isPresent()) {
                log.warn(ENTIDADE_NAO_ENCONTRADA);
                throw new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA);
            }

            doAfterLoad(optional.get());

            return optional.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // Padrão Template Method (Hook?)
    protected void doAfterLoad(E entity) throws ServiceException, ValidationException {
    };
    protected void doBeforeSave(E entity) throws ServiceException, ValidationException {
    };

    @Override
    @Transactional
    public E save(E entity) throws ServiceException, ValidationException {
        if (entity == null) {
            return null;
        }
        doBeforeSave(entity);
        log.info("Salvando entidade {}", entity);
        entity = this.repository.save(entity);
        doAfterSave(entity);
        return entity;
    }

    protected void doAfterSave(E entity) throws ServiceException, ValidationException {
    };
    protected void doBeforeUpdate(E entity) throws ServiceException, ValidationException {
    };

    @Override
    @Transactional
    public E update(E entity, ID id) throws ServiceException, ValidationException {
        if (entity == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E entityOriginal = getRepository().findById((ID) entity.getId())
                .orElseThrow(() -> new RuntimeException("Entidade com ID " + id + " não encontrada"));

        doBeforeUpdate(entity);

        log.info("Atualizando entidade {}", entity);

        BeanUtils.copyProperties(entity, entityOriginal, getNullPropertyNames(entity));

        entity = this.repository.save(entity);
        doAfterUpdate(entity);

        return entity;
    }

    protected void doAfterUpdate(E entity) throws ServiceException, ValidationException {
    };
    protected void doBeforeDelete(ID id) throws ServiceException, ValidationException {
    };

    @Override
    @Transactional
    public void delete(ID id) throws ServiceException, ValidationException {
        if (id == null) {
            throw new ServiceException(ID_NAO_INFORMADO);
        }
        doBeforeDelete(id);
        log.info("Deletando entidade id {}", id);
        Optional<E> obj = this.repository.findById(id);
        if (!obj.isPresent()) {
            throw new ServiceException(NAO_EXISTE_REGISTRO_COM_O_ID_INFORMADO);
        }
        this.repository.deleteById(id);
    }

    @Override
    public R getRepository() {
        return repository;
    }

    @Override
    public Iterable<E> getAll() {
        return this.repository.findAll(new BooleanBuilder().getValue());
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getReadMethod().isAnnotationPresent(Transient.class)) {
                continue;
            }
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    protected void throwException(Exception e, String local) throws ServiceException {
        e.printStackTrace();
        log.error(e.getMessage(), e);
    }

}
