package br.com.swapi.controllers;

import br.com.swapi.commons.response.Response;
import br.com.swapi.dto.PlanetDTO;
import br.com.swapi.services.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PlanetControllerTest {

    public static final String API_PLANETS = "/api/planets";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlanetResource planetResource;
    @MockBean
    private PlanetService planetService;

    private JacksonTester<Response> jsonResponse;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void contexLoads() {
        assertNotNull(planetResource);
        assertNotNull(planetService);
        assertNotNull(mockMvc);
    }

    @Test
    public void getAllPlanets() throws Exception {
        List<PlanetDTO> list = new ArrayList();
        list.add(PlanetDTO.builder().name("Planet1").build());
        list.add(PlanetDTO.builder().name("Planet2").build());

        when(planetService.getAll()).thenReturn(list);

        Response<List<PlanetDTO>> response = Response
                .<List<PlanetDTO>>builder()
                .data(list)
                .build();

        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(get(API_PLANETS).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.OK.value());
        assertEquals(mockHttpServletResponse.getContentAsString(), jsonResponse.write(response).getJson());
    }
}
