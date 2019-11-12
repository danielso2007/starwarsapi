package br.com.swapi.repositories;

import org.springframework.stereotype.Repository;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.entities.Planet;

@Repository
public interface PlanetRepository extends BaseRepository<Planet, String> {
}
