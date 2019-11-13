package br.com.swapi.commons.service.impl;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.GenericsUtils;
import br.com.swapi.commons.Util;
import br.com.swapi.commons.entity.BaseAudit;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.commons.service.BaseService;
import br.com.swapi.commons.type.BaseSearchTypeDTO;
import br.com.swapi.commons.type.BaseTypeDTO;
import com.mongodb.MongoClient;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.mongodb.morphia.MorphiaQuery;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.beans.Transient;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public abstract class BaseServiceImpl<E extends BaseEntity, P extends BaseSearchTypeDTO, T extends BaseTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>>
        implements BaseService<E, P, T, ID, R> {

    public static final String NAO_EXISTE_REGISTRO_COM_O_ID_INFORMADO = "Não existe registro com o ID informado.";
    public static final String ID_NAO_INFORMADO = "Id não informado.";
    public static final String FILTRO_NAO_INFORMADO = "Filtro de pesquisa nao informado.";
    public static final String ENTIDADE_NAO_INFORMADA = "Entidade não informada.";
    public static final String ENTIDADE_NAO_ENCONTRADA = "Entidade não encontrada.";

    private final R repository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("spring.data.mongodb.database")
    private String mongoDatabase;

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private String mongoPort;

    private final EntityPath<E> entityPath;
    private final Class<E> entityClass;
    private final Class<T> transferObjectClass;

    public BaseServiceImpl(R repository) {
        this.repository = repository;
        this.entityClass = GenericsUtils.getGenericsInfo(this).getType(0);
        this.transferObjectClass = GenericsUtils.getGenericsInfo(this).getType(2);
        this.entityPath = new PathBuilder<>(this.entityClass, Util.getInstance().varName(this.entityClass));
    }

    @Override
    public T getById(ID id) throws EntityNotFoundException, ValidationException {
        return map(findById(id));
    }

    @Override
    public E findById(ID id) throws EntityNotFoundException, ValidationException {
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
    }

    protected void doBeforeSave(E entity) throws ServiceException, ValidationException {
    }

	protected void doBeforeSave(E entity, T dto) throws ServiceException, ValidationException {
	}

    @Override
    public T save(T dto) throws ServiceException, ValidationException {
        if (dto == null) {
            return null;
        }
		E entity = map(dto);
		doBeforeSave(entity, dto);
		dto = map(save(entity));
        return dto;
    }

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
    }

    protected void doBeforeUpdate(E entity) throws ServiceException, ValidationException {
    }

	protected void doBeforeUpdate(E entity, T dto) throws ServiceException, ValidationException {
	}

	@Override
	public T update(T dto, ID id) throws ServiceException, ValidationException {
		E entity = map(dto);
		doBeforeUpdate(entity, dto);
		dto = map(update(entity, id));
		return dto;
	}

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
    }

    protected void doBeforeDelete(ID id) throws ServiceException, ValidationException {
    }

    @Override
    @Transactional
    public void delete(ID id) throws ServiceException, ValidationException {
        if (id == null) {
            throw new ServiceException(ID_NAO_INFORMADO);
        }
        doBeforeDelete(id);
        log.info("Deletando entidade id {}", id);
        this.repository.deleteById(id);
    }

    @Override
    public List<T> getAll() {
        return map(this.repository.findAll());
    }

    protected Pageable createPageableSearch(int page, int count) throws ServiceException {
        return PageRequest.of(page, count);
    }

    protected void createPredicated(BooleanBuilder booleanBuilder, P filter) {
    }

    // TODO: Permitir a inclusão de ordenação no parâmetro do metodo.
    @Override
    public Page<T> search(int page, int count, P filter) throws ServiceException {
        if (filter == null) {
            throw new ServiceException(FILTRO_NAO_INFORMADO);
        }
        page = page - 1;
        try {
            Pageable pageable = createPageableSearch(page, count);
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            createPredicated(booleanBuilder, filter);
            // FIXME: Estudar uma melhor abordagem para converter a pesquisa
            Page<E> pages = getRepository().findAll(booleanBuilder, pageable);
            Page<T> pagesDto = convertSearchPage(pages, pageable);
            afterSearch(pagesDto, filter);
            return pagesDto;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    protected Page<T> convertSearchPage(Page<E> page, Pageable pageable) {
        return new PageImpl(map(page.getContent()), pageable, page.getTotalElements());
    }

    protected void afterSearch(Page<T> pages, P filter) throws ServiceException {
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

    private void checkAuditedEntity(E entity) {
        if (entity instanceof BaseAudit) {
            BaseAudit auditedEntity = (BaseAudit) entity;

            // Garantir que não vieram informações do front-end.
            auditedEntity.setCreator(null);
            auditedEntity.setCreatedAt(null);
            auditedEntity.setUpdater(null);
            auditedEntity.setUpdatedAt(null);

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
    @Override
    public final MorphiaQuery<E> select() {
        MongoClient client = new MongoClient(mongoHost, Integer.parseInt(mongoPort));
        // Morphia não consegue pegar o ID na herança de classes
        Morphia morphia = new Morphia().map(this.entityClass);
        Datastore ds = morphia.createDatastore(client, mongoDatabase);
        return new MorphiaQuery<>(morphia, ds, entityPath);
    }

    @Override
    public final T map(E entity) {
        return modelMapper.map(entity, transferObjectClass);
    }

    @Override
    public final E map(T dto) {
        return modelMapper.map(dto, entityClass);
    }

    @Override
    public List<T> map(Iterator<E> iterator) {
        List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(map(iterator.next()));
        }
        return result;
    }

    @Override
    public List<T> map(List<E> list) {
        return list.stream().map(e -> map(e)).collect(Collectors.toList());
    }

    @Override
    public final R getRepository() {
        return repository;
    }

    @Override
    public final Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public final EntityPath<E> getEntityPath() {
        return entityPath;
    }

}
