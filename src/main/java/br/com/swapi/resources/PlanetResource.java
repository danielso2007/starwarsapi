package br.com.swapi.resources;

import br.com.swapi.dto.PlanetDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.swapi.commons.Constants;
import br.com.swapi.commons.resource.BaseCrudResource;
import br.com.swapi.dto.PlanetSearchDTO;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;
import br.com.swapi.services.PlanetService;

@RestController
@RequestMapping(Constants.ROOT_URL + "planets")
@CrossOrigin(origins = "*")
@Api(tags = "Planet", value = "Tudo sobre os planetas do universo Starwars", protocols = "HTTP")
public class PlanetResource extends BaseCrudResource<Planet, PlanetSearchDTO, PlanetDTO, String, PlanetRepository, PlanetService> {

	@Autowired
	public PlanetResource(PlanetService service) {
		super(service);
	}

}
