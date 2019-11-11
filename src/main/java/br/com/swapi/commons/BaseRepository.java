package br.com.swapi.commons;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import br.com.swapi.commons.entity.BaseEntity;

public interface BaseRepository<E extends BaseEntity, ID extends Serializable> extends MongoRepository<E, ID>, QuerydslPredicateExecutor<E> {
    Long countById(ID id);
}
