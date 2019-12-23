package br.com.swapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.swapi.commons.BaseRepository;
import br.com.swapi.entities.Planet;

@Repository
public interface PlanetRepository extends BaseRepository<Planet, String> {

	@Query("{_id: true, name: true}")
	List<Planet> findAllByWithProjectionIdAndName();

	Optional<Planet> findByName(String string);

	List<Planet> findByClimateIn(List<String> climates);

	@Query("{ climate: ?0, \"films\": { $lte: ?1 } }")
	List<Planet> findByClimateAndFilmsLt(String climate, Integer qtdFilms);

	@Query("{ climate: ?0, \"films\": { $gte: ?1 } }")
	List<Planet> findByClimateAndFilmsGT(String string, int i);

	<T> List<T> findByName(String name, Class<T> type);
}
