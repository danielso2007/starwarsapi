package br.com.swapi.services;

import br.com.swapi.commons.service.GenericService;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;

public interface PlanetService extends GenericService<Planet, String, PlanetRepository> {

}
