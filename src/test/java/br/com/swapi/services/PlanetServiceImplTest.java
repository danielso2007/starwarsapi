package br.com.swapi.services;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.BooleanBuilder;

import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.commons.service.impl.BaseServiceImpl;
import br.com.swapi.dto.PlanetDTO;
import br.com.swapi.dto.PlanetSearchDTO;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PlanetServiceImplTest {

	public static final String ENTIDADE_COM_ID_S_NAO_ENCONTRADA = "Entidade com ID %s não encontrada";
	private final String ID = "5dc4c9734e9b1214ed7a9e8a";
	private final String NAME = "planet";
	private Planet ENTITY;
	private List<Object> TERRAINS;

	@MockBean
	private PlanetRepository planetRepository;

	@Autowired
	private PlanetService planetService;

	@Before
	public void setUp() {
		TERRAINS = new ArrayList<>();
		String TUNDRA = "tundra";
		TERRAINS.add(TUNDRA);
		String MOUNTAINS = "mountains";
		TERRAINS.add(MOUNTAINS);

		List<Object> CLIMATES = new ArrayList<>();
		String TROPICAL = "tropical";
		CLIMATES.add(TROPICAL);
		String TEMPERATE = "temperate";
		CLIMATES.add(TEMPERATE);

		ENTITY = Planet.builder().id(ID).name(NAME).terrain(TERRAINS).climate(CLIMATES).films(0).build();

		Optional<Planet> optional = Optional.of(ENTITY);

		List<Planet> list = new ArrayList<>();
		list.add(ENTITY);

		when(planetRepository.findById(ID)).thenReturn(optional);
		when(planetRepository.save(ENTITY)).thenReturn(ENTITY);
		when(planetRepository.findAll()).thenReturn(list);

		Page<Planet> foundPage = new PageImpl<>(list);
		when(planetRepository.findAll(any(BooleanBuilder.class), any(PageRequest.class))).thenReturn(foundPage);
	}

	@Test
	public void testContexLoads() {
		Assert.assertNotNull(planetService);
		Assert.assertNotNull(planetRepository);
	}

	@Test
	public void testQuandoIdValidoEhPlanetDTOEncontrado() {
		PlanetDTO found = planetService.getById(ID);
		assertEquals(found.getId(), ID);
	}

	@Test
	public void testQuandoIdValidoEhPlanetEncontrado() {
		Planet found = planetService.findById(ID);
		assertEquals(found.getId(), ID);
	}

	@Test
	public void testQuandoIdInvalidoEhPlanetNaoEncontrado() {
		Exception exception = assertThrows(EntityNotFoundException.class,
				() -> planetService.findById(String.format("%s%s", ID, "QW23")));

		String expectedMessage = BaseServiceImpl.ENTIDADE_NAO_ENCONTRADA;
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	public void testQuandoIdNuloEhPlanetNaoEncontrado() {
		Exception exception = assertThrows(ValidationException.class, () -> planetService.findById(null));

		String expectedMessage = BaseServiceImpl.ID_NAO_INFORMADO;
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	public void testQuandoSalvandoEhPlanetRetornado() {
		Planet found = planetService.save(ENTITY);
		assertEquals(found.getId(), ID);
		assertEquals(found.getName(), ENTITY.getName());
	}

	@Test
	public void testQuandoSalvandoNuloEhPlanetNull() {
		Planet found = planetService.save((Planet) null);
		assertNull(found);
	}

	@Test
	public void testQuandoSalvandoCamposObrigatoriosDePlanet() {
		Planet entity = ENTITY;
		entity.setName(null);

		Exception exception = assertThrows(ValidationException.class, () -> planetService.save(entity));
		assertEquals(exception.getMessage(), "Nome é obrigatório");

		entity.setName(NAME);
		entity.setTerrain(null);

		exception = assertThrows(ValidationException.class, () -> planetService.save(entity));
		assertEquals(exception.getMessage(), "Terreno é obrigatório");

		entity.setTerrain(new ArrayList<>());

		exception = assertThrows(ValidationException.class, () -> planetService.save(entity));
		assertEquals(exception.getMessage(), "Terreno é obrigatório");

		entity.setTerrain(TERRAINS);
		entity.setClimate(null);

		exception = assertThrows(ValidationException.class, () -> planetService.save(entity));
		assertEquals(exception.getMessage(), "Clima é obrigatório");

		entity.setClimate(new ArrayList<>());

		exception = assertThrows(ValidationException.class, () -> planetService.save(entity));
		assertEquals(exception.getMessage(), "Clima é obrigatório");
	}

	@Test
	public void testQuandoMapeandoDTO() {
		PlanetDTO foundDTO = planetService.map(ENTITY);
		assertEquals(foundDTO.getId(), ENTITY.getId());
		assertEquals(foundDTO.getTerrain(), ENTITY.getTerrain());
		assertEquals(foundDTO.getClimate(), ENTITY.getClimate());
		assertEquals(foundDTO.getFilms(), ENTITY.getFilms());
	}

	@Test
	public void testQuandoSalvandoDTONuloEhPlanetNull() {
		PlanetDTO foundDTO = planetService.save((PlanetDTO) null);
		assertNull(foundDTO);
	}

	@Test
	public void testQuandoAtualizandoEhPlanetEncontrado() {
		String name = "Planet3";
		ENTITY.setName(name);
		Planet found = planetService.update(ENTITY, ID);
		assertEquals(found.getId(), ID);
		assertEquals(found.getName(), name);
	}

	@Test
	public void testQuandoAtualizandoNuloEhPlanetNulo() {
		Planet found = planetService.update((Planet) null, ID);
		assertNull(found);
	}

	@Test
	public void testQuandoAtualizandoNuloIdNuloEhPlanetNulo() {
		Planet found = planetService.update((Planet) null, null);
		assertNull(found);
	}

	@Test
	public void testQuandoAtualizandoIdNaoExistenteEhPlanetNaoEncontrado() {
		String id = "5dc4c973ET651214ed7a9e8a";
		Exception exception = assertThrows(EntityNotFoundException.class, () -> planetService.update(ENTITY, id));
		assertEquals(exception.getMessage(), format(ENTIDADE_COM_ID_S_NAO_ENCONTRADA, id));
	}

	@Test
	public void testQuandoAtualizandoIdNuloEhPlanetNulo() {
		Exception exception = assertThrows(EntityNotFoundException.class, () -> planetService.update(ENTITY, null));
		assertEquals(exception.getMessage(), format(ENTIDADE_COM_ID_S_NAO_ENCONTRADA, null));
	}

	@Test
	public void testQuandoDeletandoUmPlanet() {
		planetService.delete(ID);
	}

	@Test
	public void testQuandoDeletandoUmPlanetEhIdNulo() {
		try {
			planetService.delete(null);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), BaseServiceImpl.ID_NAO_INFORMADO);
		}
	}

	@Test
	public void testQuandoObterTodosOsRegistrosPlanet() {
		List<PlanetDTO> list = planetService.getAll();
		assertNotNull(list);
		assertEquals(list.size(), 1);
	}

	@Test
	public void testQuandoPesquisandoOsRegistrosPlanet() {
		PlanetSearchDTO planetSearchDTO = PlanetSearchDTO.builder().name(NAME).build();
		Page<PlanetDTO> page = planetService.search(1, 2, planetSearchDTO);
		assertNotNull(page);
		assertEquals(page.getContent().size(), 1);
	}

	@Test
	public void testQuandoPesquisandoEhPageZero() {
		PlanetSearchDTO planetSearchDTO = PlanetSearchDTO.builder().name(NAME).build();
		Page<PlanetDTO> page = planetService.search(0, 2, planetSearchDTO);
		assertNotNull(page);
		assertEquals(page.getContent().size(), 1);
	}

	@Test
	public void testQuandoPesquisandoEhPageNegativo() {
		PlanetSearchDTO planetSearchDTO = PlanetSearchDTO.builder().name(NAME).build();
		Page<PlanetDTO> page = planetService.search(-1, 2, planetSearchDTO);
		assertNotNull(page);
		assertEquals(page.getContent().size(), 1);
	}

	@Test
	public void testQuandoPesquisandoEhCountZero() {
		PlanetSearchDTO planetSearchDTO = PlanetSearchDTO.builder().name(NAME).build();
		Page<PlanetDTO> page = planetService.search(1, 0, planetSearchDTO);
		assertNotNull(page);
		assertEquals(page.getContent().size(), 1);
	}

	@Test
	public void testQuandoPesquisandoEhCountNegativo() {
		PlanetSearchDTO planetSearchDTO = PlanetSearchDTO.builder().name(NAME).build();
		Page<PlanetDTO> page = planetService.search(1, -1, planetSearchDTO);
		assertNotNull(page);
		assertEquals(page.getContent().size(), 1);
	}

	@Test
	public void testQuandoPesquisandoComFiltroNulo() {
		try {
			planetService.search(1, 2, null);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), BaseServiceImpl.FILTRO_NAO_INFORMADO);
		}
	}

}
