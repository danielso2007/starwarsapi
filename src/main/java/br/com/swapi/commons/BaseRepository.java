package br.com.swapi.commons;

import br.com.swapi.commons.entity.BaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * O repositório base da aplicação.
 *
 * @param <E>  A entidade.
 * @param <ID> O tipo da identificação das entidades.
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity, ID extends Serializable> extends MongoRepository<E, ID>, QuerydslPredicateExecutor<E> {
}
