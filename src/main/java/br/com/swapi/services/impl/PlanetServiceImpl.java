package br.com.swapi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import br.com.swapi.commons.service.impl.BaseServiceImpl;
import br.com.swapi.dto.PlanetSearchDTO;
import br.com.swapi.entities.Planet;
import br.com.swapi.entities.QPlanet;
import br.com.swapi.repositories.PlanetRepository;
import br.com.swapi.services.PlanetService;

@Service
public class PlanetServiceImpl extends BaseServiceImpl<Planet, PlanetSearchDTO, String, PlanetRepository>
		implements PlanetService {

	@Autowired
	public PlanetServiceImpl(PlanetRepository repository) {
		super(repository);
	}

	@Override
	protected void createPredicated(BooleanBuilder booleanBuilder, PlanetSearchDTO filter) {
		if (filter.getId() != null) {
			booleanBuilder.and(QPlanet.planet.id.eq(filter.getId()));
		}
		// FIXME: Criar utilitário para verificação de campos
		if (filter.getName() != null && !filter.getName().trim().equals("")) {
			booleanBuilder.and(QPlanet.planet.name.containsIgnoreCase(filter.getName()));
		}
		// FIXME: Criar utilitário para verificação de campos
		if (filter.getTerrain() != null && !filter.getTerrain().trim().equals("")) {
			booleanBuilder.and(QPlanet.planet.terrain.contains(filter.getTerrain()));
		}
		// FIXME: Criar utilitário para verificação de campos
		if (filter.getClimate() != null && !filter.getClimate().trim().equals("")) {
			booleanBuilder.and(QPlanet.planet.climate.contains(filter.getClimate()));
		}
	}

}
