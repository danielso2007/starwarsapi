package br.com.swapi.services.impl;

import br.com.swapi.commons.Util;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.dto.PlanetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import br.com.swapi.commons.service.impl.BaseServiceImpl;
import br.com.swapi.dto.PlanetSearchDTO;
import br.com.swapi.entities.Planet;
import br.com.swapi.entities.QPlanet;
import br.com.swapi.repositories.PlanetRepository;
import br.com.swapi.services.PlanetService;

import javax.validation.ValidationException;

@Service
public class PlanetServiceImpl extends BaseServiceImpl<Planet, PlanetSearchDTO, PlanetDTO, String, PlanetRepository>
		implements PlanetService {

	@Autowired
	public PlanetServiceImpl(PlanetRepository repository) {
		super(repository);
	}

	@Override
	protected void createPredicated(BooleanBuilder booleanBuilder, PlanetSearchDTO filter) {
		if (Util.getInstance().isNotEmpty(filter.getId())) {
			booleanBuilder.and(QPlanet.planet.id.eq(filter.getId()));
		}
		if (Util.getInstance().isNotEmpty(filter.getName())) {
			booleanBuilder.and(QPlanet.planet.name.containsIgnoreCase(filter.getName()));
		}
		if (Util.getInstance().isNotEmpty(filter.getTerrain())) {
			booleanBuilder.and(QPlanet.planet.terrain.contains(filter.getTerrain()));
		}
		if (Util.getInstance().isNotEmpty(filter.getClimate())) {
			booleanBuilder.and(QPlanet.planet.climate.contains(filter.getClimate()));
		}
	}

	// FIXME: Existe uma validação no DTO. Mas está sendo replicado aqui para a entidade.
	//  Existe outra forma de validação sem a utilização do método abaixo?
	private void validateOnSave(Planet entity) throws ValidationException {
		if (Util.getInstance().isNotEmpty(entity.getName())) {
			throw new ValidationException("Nome é obrigatório");
		}
		if (Util.getInstance().isNotEmpty(entity.getTerrain())) {
			throw new ValidationException("Terreno é obrigatório");
		}
		if (Util.getInstance().isNotEmpty(entity.getClimate())) {
			throw new ValidationException("Clima é obrigatório");
		}
	}

	@Override
	protected void doBeforeSave(Planet entity) throws ServiceException, ValidationException {
		validateOnSave(entity);
	}
}
