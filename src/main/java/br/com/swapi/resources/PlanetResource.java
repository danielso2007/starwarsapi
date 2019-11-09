package br.com.swapi.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.swapi.commons.Constants;
import br.com.swapi.commons.resource.BaseCrudResource;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;
import br.com.swapi.services.PlanetService;

@RestController
@RequestMapping(Constants.ROOT_URL + "planets")
@CrossOrigin(origins = "*")
public class PlanetResource extends BaseCrudResource<Planet, String, PlanetRepository, PlanetService> {

	@Autowired
	public PlanetResource(PlanetService service) {
		super(service);
	}

}
