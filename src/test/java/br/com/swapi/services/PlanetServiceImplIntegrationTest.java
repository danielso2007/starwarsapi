package br.com.swapi.services;

import br.com.swapi.commons.lang.EntityNotFoundException;
import br.com.swapi.entities.Planet;
import br.com.swapi.repositories.PlanetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PlanetServiceImplIntegrationTest {

    private String ID = "5dc4c9734e9b1214ed7a9e8a";
    private String NAME = "planet";
    private String TUNDRA = "tundra";
    private String TROPICAL = "tropical";
    private String MOUNTAINS = "mountains";
    private String TEMPERATE = "temperate";
    private Planet ENTITY;
    private List<Object> TERRAINS;
    private List<Object> CLIMATES;


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

        Mockito.when(planetRepository.findById(ID)).thenReturn(optional);
        Mockito.when(planetRepository.save(ENTITY)).thenReturn(ENTITY);
    }

    @MockBean
    private PlanetRepository planetRepository;

    @Autowired
    private PlanetService planetService;

    @Test
    public void quandoIdValidoEhPlanetEncontrado() {
        Planet found = planetService.findById(ID);
        assertEquals(found.getId(), ID);
    }

    @Test
    public void quandoIdInvalidoEhPlanetNaoEncontrado() {
        try {
            Planet noFound = planetService.findById(ID + "QW23");
        } catch (EntityNotFoundException e) {
            assertTrue(e instanceof EntityNotFoundException);
            assertEquals(e.getMessage(), "Entidade não encontrada.");
        }
    }

    @Test
    public void quandoIdNuloEhPlanetNaoEncontrado() {
        try {
            Planet noFound = planetService.findById(null);
        } catch (ValidationException e) {
            assertTrue(e instanceof ValidationException);
            assertEquals(e.getMessage(), "Id não informado.");
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
            Planet found = planetService.save(entity);
        } catch (ValidationException e) {
            assertTrue(e instanceof ValidationException);
            assertEquals(e.getMessage(), "Nome é obrigatório");
        }
        entity.setName(NAME);
        entity.setTerrain(null);
        try {
            Planet found = planetService.save(entity);
        } catch (ValidationException e) {
            assertTrue(e instanceof ValidationException);
            assertEquals(e.getMessage(), "Terreno é obrigatório");
        }
        entity.setTerrain(new ArrayList<>());
        try {
            Planet found = planetService.save(entity);
        } catch (ValidationException e) {
            assertTrue(e instanceof ValidationException);
            assertEquals(e.getMessage(), "Terreno é obrigatório");
        }
        entity.setTerrain(TERRAINS);
        entity.setClimate(null);
        try {
            Planet found = planetService.save(entity);
        } catch (ValidationException e) {
            assertTrue(e instanceof ValidationException);
            assertEquals(e.getMessage(), "Clima é obrigatório");
        }
        entity.setClimate(new ArrayList<>());
        try {
            Planet found = planetService.save(entity);
        } catch (ValidationException e) {
            assertTrue(e instanceof ValidationException);
            assertEquals(e.getMessage(), "Clima é obrigatório");
        }
    }

}
