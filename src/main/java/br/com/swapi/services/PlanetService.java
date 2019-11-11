package br.com.swapi.services;

import br.com.swapi.commons.service.BaseService;
import br.com.swapi.dto.PlanetDTO;
import br.com.swapi.dto.PlanetSearchDTO;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;

public interface PlanetService extends BaseService<Planet, PlanetSearchDTO, PlanetDTO, String, PlanetRepository> {

}
