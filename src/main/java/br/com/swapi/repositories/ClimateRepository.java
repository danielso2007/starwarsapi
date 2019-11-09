package br.com.swapi.repositories;

import org.springframework.stereotype.Repository;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.entities.Climate;

@Repository
public interface ClimateRepository extends BaseRepository<Climate, String> {

}
