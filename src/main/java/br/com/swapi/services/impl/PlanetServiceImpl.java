package br.com.swapi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.swapi.commons.service.impl.GenericServiceImpl;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;
import br.com.swapi.services.PlanetService;

@Service
public class PlanetServiceImpl extends GenericServiceImpl<Planet, String, PlanetRepository> implements PlanetService {

	@Autowired
	public PlanetServiceImpl(PlanetRepository repository) {
		super(repository);
	}

}
