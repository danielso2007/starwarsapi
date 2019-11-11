package br.com.swapi.commons.service.impl;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.ValidationException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.MongoClient;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.mongodb.morphia.MorphiaQuery;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.GenericsInfo;
import br.com.swapi.commons.GenericsUtils;
import br.com.swapi.commons.Util;
import br.com.swapi.commons.entity.BaseAudit;
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

	@Value("spring.data.mongodb.database")
	private String mongoDatabase;

	@Value("${spring.data.mongodb.host}")
	private String mongoHost;

	@Value("${spring.data.mongodb.port}")
	private String mongoPort;

	protected final EntityPath<E> entityPath;
	private Class<E> entityClass;

	public GenericServiceImpl(R repository) {
		this.repository = repository;
		GenericsInfo genericsInfo = GenericsUtils.getGenericsInfo(this);
		this.entityClass = genericsInfo.getType(0);
		this.entityPath = new PathBuilder<E>(this.entityClass, Util.varName(this.entityClass));
	}

	@Override
	public Class<E> getEntityClass() {
		return entityClass;
	}

	@Override
	public E getById(ID id) throws EntityNotFoundException, ValidationException {
		if (id == null) {
			throw new ValidationException(ID_NAO_INFORMADO);
		}
		try {
			log.info("Obter {} pelo id {}.", entityClass.getName(), id);
			Optional<E> optional = this.repository.findById(id);

			if (!optional.isPresent()) {
				log.warn(ENTIDADE_NAO_ENCONTRADA);
				throw new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA);
			}

			doAfterLoad(optional.get());

			return optional.get();
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException(e.getMessage());
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

		checkAuditedEntity(entity);

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
	public E update(E entity, ID id) throws ServiceException, ValidationException, EntityNotFoundException {
		if (entity == null) {
			return null;
		}

		E entityOriginal = getRepository().findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Entidade com ID " + id + " não encontrada"));

		doBeforeUpdate(entity);

		log.info("Atualizando entidade {}", entity);

		BeanUtils.copyProperties(entity, entityOriginal, getNullPropertyNames(entity));

		checkAuditedEntity(entityOriginal);

		entity = this.repository.save(entityOriginal);

		doAfterUpdate(entityOriginal);

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
		return this.repository.findAll();
	}

	/*
	 * Referência
	 * https://www.it-swarm.net/pt/java/como-ignorar-valores-nulos-usando-
	 * springframework-beanutils-copyproperties/1043455506/
	 */
	private static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			if (pd.getReadMethod().isAnnotationPresent(Transient.class)) {
				continue;
			}
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	protected void throwException(Exception e, String local) throws ServiceException {
		e.printStackTrace();
		log.error(e.getMessage(), e);
	}

	private void checkAuditedEntity(E entity) {
		if (entity instanceof BaseAudit) {
			BaseAudit auditedEntity = (BaseAudit) entity;
			Date date = new Date();
			if (auditedEntity.getId() == null) {
				auditedEntity.setCreatedAt(date);
				// TODO: Quando houver segurança, adicionar o usuário logado
				auditedEntity.setCreator("Anonymous");
			} else {
				auditedEntity.setUpdatedAt(date);
				// TODO: Quando houver segurança, adicionar o usuário logado
				auditedEntity.setUpdater("Anonymous");
			}
		}
	}

	// TODO: Melhorar as consultas com QueryDSL.
	protected final MorphiaQuery<E> select() {
		MongoClient client = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
		// Morphia não consegue pegar o ID na herança de classes
		Morphia morphia = new Morphia().map(this.entityClass);
		Datastore ds = morphia.createDatastore(client, mongoDatabase);
		return new MorphiaQuery<E>(morphia, ds, entityPath);
	}

}
