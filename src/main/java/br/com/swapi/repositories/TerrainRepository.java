package br.com.swapi.repositories;

import org.springframework.stereotype.Repository;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.entities.Terrain;

@Repository
public interface TerrainRepository extends BaseRepository<Terrain, String> {

}
