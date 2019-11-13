package br.com.swapi.services;

import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.commons.lang.ServiceException;
import br.com.swapi.commons.service.impl.BaseServiceImpl;
import br.com.swapi.dto.PlanetDTO;
import br.com.swapi.dto.PlanetSearchDTO;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;
import com.querydsl.core.BooleanBuilder;
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

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PlanetServiceImplTest {

    private String ID = "5dc4c9734e9b1214ed7a9e8a";
    private String NAME = "planet";
    private String TUNDRA = "tundra";
    private String TROPICAL = "tropical";
    private String MOUNTAINS = "mountains";
    private String TEMPERATE = "temperate";
    private Planet ENTITY;
    private List<Object> TERRAINS;
    private List<Object> CLIMATES;

    @MockBean
    private PlanetRepository planetRepository;

    @Autowired
    private PlanetService planetService;

    @Before
    public void setUp() {
        TERRAINS = new ArrayList();
        TERRAINS.add(TUNDRA);
        TERRAINS.add(MOUNTAINS);

        CLIMATES = new ArrayList();
        CLIMATES.add(TROPICAL);
        CLIMATES.add(TEMPERATE);

        ENTITY = Planet
                .builder()
                .id(ID)
                .name(NAME)
                .terrain(TERRAINS)
                .climate(CLIMATES)
                .films(0)
                .build();

        Optional<Planet> optional = Optional.of(ENTITY);

        List<Planet> list = new ArrayList<>();
        list.add(ENTITY);

        when(planetRepository.findById(ID)).thenReturn(optional);
        when(planetRepository.save(ENTITY)).thenReturn(ENTITY);
        when(planetRepository.findAll()).thenReturn(list);

        Page<Planet> foundPage = new PageImpl(list);
        when(planetRepository.findAll(any(BooleanBuilder.class), any(PageRequest.class))).thenReturn(foundPage);
    }

    private void validationError(Exception e, String message) {
        assertTrue(e instanceof ValidationException);
        assertEquals(e.getMessage(), message);
    }

    private void entityNotFoundExceptionError(Exception e, String message) {
        assertTrue(e instanceof EntityNotFoundException);
        assertEquals(e.getMessage(), message);
    }

    @Test
    public void contexLoads() {
        Assert.assertNotNull(planetService);
        Assert.assertNotNull(planetRepository);
    }

    @Test
    public void quandoIdValidoEhPlanetDTOEncontrado() {
        PlanetDTO found = planetService.getById(ID);
        assertEquals(found.getId(), ID);
    }

    @Test
    public void quandoIdValidoEhPlanetEncontrado() {
        Planet found = planetService.findById(ID);
        assertEquals(found.getId(), ID);
    }

    @Test
    public void quandoIdInvalidoEhPlanetNaoEncontrado() {
        try {
            planetService.findById(String.format("%s%s", ID, "QW23"));
        } catch (EntityNotFoundException e) {
            entityNotFoundExceptionError(e, BaseServiceImpl.ENTIDADE_NAO_ENCONTRADA);
        }
    }

    @Test
    public void quandoIdNuloEhPlanetNaoEncontrado() {
        try {
            planetService.findById(null);
        } catch (ValidationException e) {
            validationError(e, BaseServiceImpl.ID_NAO_INFORMADO);
        }
    }

    @Test
    public void quandoSalvandoEhPlanetRetornado() {
        Planet found = planetService.save(ENTITY);
        assertEquals(found.getId(), ID);
        assertEquals(found.getName(), ENTITY.getName());
    }

    @Test
    public void quandoSalvandoNuloEhPlanetNull() {
        Planet found = planetService.save((Planet) null);
        assertNull(found);
    }

    @Test
    public void quandoSalvandoCamposObrigatoriosDePlanet() {
        Planet entity = ENTITY;
        entity.setName(null);
        try {
            planetService.save(entity);
        } catch (ValidationException e) {
            validationError(e, "Nome é obrigatório");
        }
        entity.setName(NAME);
        entity.setTerrain(null);
        try {
            planetService.save(entity);
        } catch (ValidationException e) {
            validationError(e, "Terreno é obrigatório");
        }
        entity.setTerrain(new ArrayList<>());
        try {
            planetService.save(entity);
        } catch (ValidationException e) {
            validationError(e, "Terreno é obrigatório");
        }
        entity.setTerrain(TERRAINS);
        entity.setClimate(null);
        try {
            planetService.save(entity);
        } catch (ValidationException e) {
            validationError(e, "Clima é obrigatório");
        }
        entity.setClimate(new ArrayList<>());
        try {
            planetService.save(entity);
        } catch (ValidationException e) {
            validationError(e, "Clima é obrigatório");
        }
    }

    @Test
    public void quandoMapeandoDTO() {
        PlanetDTO foundDTO = planetService.map(ENTITY);
        assertEquals(foundDTO.getId(), ENTITY.getId());
        assertEquals(foundDTO.getTerrain(), ENTITY.getTerrain());
        assertEquals(foundDTO.getClimate(), ENTITY.getClimate());
        assertEquals(foundDTO.getFilms(), ENTITY.getFilms());
    }

    @Test
    public void quandoSalvandoDTONuloEhPlanetNull() {
        PlanetDTO foundDTO = planetService.save((PlanetDTO) null);
        assertNull(foundDTO);
    }

    @Test
    public void quandoAtualizandoEhPlanetEncontrado() {
        String name = "Planet3";
        ENTITY.setName(name);
        Planet found = planetService.update(ENTITY, ID);
        assertEquals(found.getId(), ID);
        assertEquals(found.getName(), name);
    }

    @Test
    public void quandoAtualizandoNuloEhPlanetNulo() {
        Planet found = planetService.update((Planet) null, ID);
        assertNull(found);
    }

    @Test
    public void quandoAtualizandoNuloIdNuloEhPlanetNulo() {
        Planet found = planetService.update((Planet) null, null);
        assertNull(found);
    }

    @Test
    public void quandoAtualizandoIdNaoExistenteEhPlanetNaoEncontrado() {
        String id = "5dc4c973ET651214ed7a9e8a";
        try {
            planetService.update(ENTITY, id);
        } catch (EntityNotFoundException e) {
            entityNotFoundExceptionError(e, String.format("Entidade com ID %s não encontrada", id));
        }
    }

    @Test
    public void quandoAtualizandoIdNuloEhPlanetNulo() {
        try {
            planetService.update(ENTITY, null);
        } catch (EntityNotFoundException e) {
            entityNotFoundExceptionError(e, String.format("Entidade com ID %s não encontrada", (Object) null));
        }
    }

    @Test
    public void quandoDeletandoUmPlanet() {
        planetService.delete(ID);
    }

    @Test
    public void quandoDeletandoUmPlanetEhIdNulo() {
        try {
            planetService.delete(null);
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), BaseServiceImpl.ID_NAO_INFORMADO);
        }
    }

    @Test
    public void quandoObterTodosOsRegistrosPlanet() {
        List<PlanetDTO> list = planetService.getAll();
        assertNotNull(list);
        assertEquals(list.size(), 1);
    }

    @Test
    public void quandoPesquisandoOsRegistrosPlanet() {
        PlanetSearchDTO planetSearchDTO = PlanetSearchDTO
                .builder()
                .name(NAME)
                .build();
        Page<PlanetDTO> page = planetService.search(1, 2, planetSearchDTO);
        assertNotNull(page);
        assertEquals(page.getContent().size(), 1);
    }

    @Test
    public void quandoPesquisandoComFiltroNulo() {
        try {
            planetService.search(1, 2, null);
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), BaseServiceImpl.FILTRO_NAO_INFORMADO);
        }
    }

}
