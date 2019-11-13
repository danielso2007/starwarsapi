package br.com.swapi.commons.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.validation.ValidationException;

import br.com.swapi.commons.type.BaseTypeDTO;
import com.querydsl.mongodb.morphia.MorphiaQuery;
import org.springframework.data.domain.Page;

import com.querydsl.core.types.EntityPath;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.commons.entity.BaseEntity;
import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.commons.type.BaseSearchTypeDTO;

/**
 * Serviço base.
 * @param <E> A entidade.
 * @param <P> O DTO de pesquisa.
 * @param <T> O DTO.
 * @param <ID> O tipo de identificador.
 * @param <R> O repositório da entidade.
 */
public interface BaseService<E extends BaseEntity, P extends BaseSearchTypeDTO, T extends BaseTypeDTO, ID extends Serializable, R extends BaseRepository<E, ID>> {

	/**
	 * Obter um registro na base de dados.
	 * @param id
	 * @return O registro da base de dados.
	 * @throws EntityNotFoundException Quando o registro não for encontrado.
	 * @throws ValidationException Erro de validação.
	 */
	E findById(ID id) throws EntityNotFoundException, ValidationException;

	/**
	 * Salvar um novo registro.
	 * @param entity O registro a ser salvo.
	 * @return O registro da base de dados.
	 * @throws ServiceException Erro de sistema.
	 * @throws ValidationException Erro de validação.
	 */
	E save(E entity) throws ServiceException, ValidationException;

	/**
	 * Atualiza um registro na base de dados.
	 * @param entity O registro a ser atualizado.
	 * @param id O identificador do registro.
	 * @return O registro atualizado.
	 * @throws EntityNotFoundException Quando o registro não for encontrado.
	 * @throws ValidationException Erro de validação.
	 * @throws ServiceException Erro de sistema.
	 */
	E update(E entity, ID id) throws EntityNotFoundException, ValidationException, ServiceException;

	/**
	 * Obter um registro DTO na base de dados.
	 * Mais usado no recursos da aplicação.
	 * @param id
	 * @return O registro da base de dados.
	 * @throws EntityNotFoundException Quando o registro não for encontrado.
	 * @throws ValidationException Erro de validação.
	 */
	T getById(ID id) throws EntityNotFoundException, ValidationException;

	/**
	 * Salvar um novo registro DTO.
	 * Mais usado no recursos da aplicação.
	 * @param entity O registro a ser salvo.
	 * @return O registro DTO da base de dados.
	 * @throws ServiceException Erro de sistema.
	 * @throws ValidationException Erro de validação.
	 */
	T save(T entity) throws ServiceException, ValidationException;

	/**
	 * Atualiza um registro DTO na base de dados.
	 * Mais usado no recursos da aplicação.
	 * @param entity O registro a ser atualizado.
	 * @param id O identificador do registro.
	 * @return O registro atualizado.
	 * @throws EntityNotFoundException Quando o registro não for encontrado.
	 * @throws ValidationException Erro de validação.
	 * @throws ServiceException Erro de sistema.
	 */
	T update(T entity, ID id) throws EntityNotFoundException, ValidationException, ServiceException;

	/**
	 * Remove um registro na base de dados.
	 * @param id O identificador do registro.
	 * @throws EntityNotFoundException Quando o registro não for encontrado.
	 * @throws ServiceException Erro de validação.
	 */
	void delete(ID id) throws EntityNotFoundException, ServiceException;

	/**
	 * Obter todos os registros da base de dados.
	 * @return A lista de registros da base de dados.
	 */
	List<T> getAll();

	/**
	 * Pesquisar por registros na base de dados.
	 * @param page A página a ser pesquisada.
	 * @param count A quantidade de registros por página.
	 * @param filter O filtro de pesquisa.
	 * @return Retorna a lista de registros filtrada.
	 */
	Page<T> search(int page, int count, P filter);

	/**
	 * O repositório da entidade.
	 * @return O repositório da entidade.
	 */
	R getRepository();

	/**
	 * O path do queryDsl que representa a entidade.
	 * @return O path do queryDsl que representa a entidade.
	 */
	EntityPath<E> getEntityPath();

	/**
	 * O tipo de classe da entidade.
	 * @return O tipo de classe da entidade.
	 */
	Class<E> getEntityClass();

	MorphiaQuery<E> select();

	T map(E entity);

	E map(T dto);

	List<T> map(Iterator<E> iterator);

	List<T> map(List<E> list);

}
