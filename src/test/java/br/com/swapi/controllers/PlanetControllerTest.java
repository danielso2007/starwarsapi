package br.com.swapi.controllers;

import br.com.swapi.commons.Constants;
import br.com.swapi.commons.response.Response;
import br.com.swapi.commons.test.RestResponsePage;
import br.com.swapi.dto.PlanetDTO;
import br.com.swapi.dto.PlanetSearchDTO;
import br.com.swapi.services.PlanetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PlanetControllerTest {

    public static final String API_PLANETS = "/api/planets";
    private String ID = "5dc4c9734e9b1214ed7a9e8a";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlanetResource planetResource;
    @MockBean
    private PlanetService planetService;

    private JacksonTester<Response> jsonResponse;
    private JacksonTester<PlanetSearchDTO> jsonFilter;
    private JacksonTester<Page<PlanetDTO>> jsonPage;
    private JacksonTester<PlanetDTO> jsonPlanet;

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

    // TODO: Criar no framework para outros testes utilizarem.
    private MockHttpServletResponse getHttpServletResponse(String url, ResultMatcher status) throws Exception {
        return httpServletResponse(HttpMethod.GET, url, null, status);
    }

    // TODO: Criar no framework para outros testes utilizarem.
    private MockHttpServletResponse putHttpServletResponse(String url, String json, ResultMatcher status) throws Exception {
        return httpServletResponse(HttpMethod.PUT, url, json, status);
    }

    // TODO: Criar no framework para outros testes utilizarem.
    private MockHttpServletResponse deleteHttpServletResponse(String url, String json, ResultMatcher status) throws Exception {
        return httpServletResponse(HttpMethod.DELETE, url, json, status);
    }

    // TODO: Criar no framework para outros testes utilizarem.
    private MockHttpServletResponse postHttpServletResponse(String url, String json, ResultMatcher status) throws Exception {
        return httpServletResponse(HttpMethod.POST, url, json, status);
    }

    // TODO: Criar no framework para outros testes utilizarem.
    private MockHttpServletResponse httpServletResponse(HttpMethod httpMethod,String url, String json, ResultMatcher status) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = null;

        if (HttpMethod.POST.equals(httpMethod)) {
            requestBuilder = post(url).contentType(Constants.APPLICATION_JSON_UTF_8);
        } else if (HttpMethod.GET.equals(httpMethod)) {
            requestBuilder = get(url).contentType(Constants.APPLICATION_JSON_UTF_8);
        } else if (HttpMethod.PUT.equals(httpMethod)) {
            requestBuilder = put(url).contentType(Constants.APPLICATION_JSON_UTF_8);
        } else if (HttpMethod.DELETE.equals(httpMethod)) {
            requestBuilder = delete(url).contentType(Constants.APPLICATION_JSON_UTF_8);
        }

        if (json != null) {
            requestBuilder.content(json);
        }

        MockHttpServletResponse mockHttpServletResponse = mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status)
                .andReturn()
                .getResponse();
        return mockHttpServletResponse;
    }

    @Test
    public void testGetAllPlanets() throws Exception {
        List<PlanetDTO> list = new ArrayList();
        list.add(PlanetDTO.builder().name("Planet1").build());
        list.add(PlanetDTO.builder().name("Planet2").build());

        when(planetService.getAll()).thenReturn(list);

        Response<List<PlanetDTO>> response = Response
                .<List<PlanetDTO>>builder()
                .data(list)
                .build();

        assertEquals(getHttpServletResponse(
                API_PLANETS, status().isOk()).getContentAsString(),
                jsonResponse.write(response).getJson());
    }

    @Test
    public void testSearchPlanets() throws Exception {
        List<PlanetDTO> list = new ArrayList();
        list.add(PlanetDTO.builder().name("Planet1").build());
        list.add(PlanetDTO.builder().name("Planet2").build());

        Page<PlanetDTO> foundPage = new PageImpl(list, PageRequest.of(1, 5), 1);

        when(planetService.search(
                any(Integer.class),
                any(Integer.class),
                any(PlanetSearchDTO.class)))
                .thenReturn(foundPage);

        String json = postHttpServletResponse(
                String.format("%s/search/%d/%d", API_PLANETS, 1, 5),
                jsonFilter.write(PlanetSearchDTO.builder().build()).getJson(),
                status().isOk()).getContentAsString();

        RestResponsePage pageResponse = new ObjectMapper().readValue(json, RestResponsePage.class);

        // FIXME: Essas conversões são a melhor prática?
        assertEquals(jsonPage.write(pageResponse).getJson(), jsonPage.write(foundPage).getJson());
    }

    @Test
    public void testSearchPlanetsNoContent() throws Exception {
        postHttpServletResponse(
                String.format("%s/search/%d/%d", API_PLANETS, 1, 5),
                null,
                status().isBadRequest());
    }

    @Test
    public void testbyIdPlanets() throws Exception {
        PlanetDTO planetDTO = PlanetDTO.builder().name("Planet1").id(ID).build();

        when(planetService.getById(any(String.class))).thenReturn(planetDTO);

        Response<PlanetDTO> response = Response
                .<PlanetDTO>builder()
                .data(planetDTO)
                .build();

        String json = getHttpServletResponse(
                String.format("%s/%s", API_PLANETS, ID),
                status().isOk()).getContentAsString();

        assertEquals(json, jsonResponse.write(response).getJson());
    }

    @Test
    public void testSalvandoPlanet() throws Exception {
        PlanetDTO planetDTOResponse = PlanetDTO
                .builder()
                .id(ID)
                .name("Planet1")
                .terrain(new ArrayList<Object>() {{
                    add("tundra");
                }})
                .climate(new ArrayList<Object>() {{
                    add("tropical");
                }})
                .films(3)
                .build();

        PlanetDTO planetDTORequest = PlanetDTO
                .builder()
                .name("Planet1")
                .terrain(new ArrayList<Object>() {{
                    add("tundra");
                }})
                .climate(new ArrayList<Object>() {{
                    add("tropical");
                }})
                .films(3)
                .build();

        when(planetService.save(any(PlanetDTO.class))).thenReturn(planetDTORequest);

        Response<PlanetDTO> response = Response
                .<PlanetDTO>builder()
                .data(planetDTORequest)
                .build();

        String json = postHttpServletResponse(
                String.format("%s", API_PLANETS),
                jsonPlanet.write(planetDTOResponse).getJson(),
                status().isOk()).getContentAsString();

        assertEquals(json, jsonResponse.write(response).getJson());
    }

    private MockHttpServletResponse saveHttpServletResponse(PlanetDTO planetDto) throws Exception {
        return postHttpServletResponse(
                String.format("%s", API_PLANETS),
                jsonPlanet.write(planetDto).getJson(),
                status().isBadRequest());
    }

    private Response getSaveResponse(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Response.class);
    }

    @Test
    public void testValidandoCamposObrigatoriosSalvandoPlanet() throws Exception {
        PlanetDTO planetDto = PlanetDTO
                .builder()
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Nome é obrigatório"));
        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Nome não pode ser nulo"));

        planetDto = PlanetDTO
                .builder()
                .name("Phdjfurhdbfheudjfnfhdj")
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Nome deve ter entre 2 e 20 caracteres"));

        planetDto = PlanetDTO
                .builder()
                .name("P")
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Nome deve ter entre 2 e 20 caracteres"));

        planetDto = PlanetDTO
                .builder()
                .name("")
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Nome é obrigatório"));

        planetDto = PlanetDTO
                .builder()
                .name("Planet test")
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Terreno é obrigatório"));

        planetDto = PlanetDTO
                .builder()
                .name("Planet test")
                .terrain(new ArrayList<Object>() {{
                    add("tundra");
                }})
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Clima é obrigatório"));

        planetDto = PlanetDTO
                .builder()
                .name("Planet test")
                .terrain(new ArrayList<Object>() {{
                    add("tundra");
                }})
                .climate(new ArrayList<Object>() {{
                    add("tropical");
                }})
                .films(-1)
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Quantidades de filmes não pode ser menor que 0"));

        planetDto = PlanetDTO
                .builder()
                .name("Planet test")
                .terrain(new ArrayList<Object>() {{
                    add("tundra");
                }})
                .climate(new ArrayList<Object>() {{
                    add("tropical");
                }})
                .films(40)
                .build();

        assertTrue(getSaveResponse(saveHttpServletResponse(planetDto).getContentAsString()).getErrors().contains("Quantidades de filmes não pode ser maior que 10"));
    }

    @Test
    public void testAtualizandoPlanet() throws Exception {
        PlanetDTO planetDTOResponse = PlanetDTO
                .builder()
                .name("Planet teste")
                .build();

        PlanetDTO planetDTORequest = PlanetDTO
                .builder()
                .id(ID)
                .name("Planet teste")
                .terrain(new ArrayList<Object>() {{
                    add("tundra");
                }})
                .climate(new ArrayList<Object>() {{
                    add("tropical");
                }})
                .films(3)
                .build();

        when(planetService.update(any(PlanetDTO.class), any(String.class))).thenReturn(planetDTORequest);

        Response<PlanetDTO> response = Response
                .<PlanetDTO>builder()
                .data(planetDTORequest)
                .build();

        String json = putHttpServletResponse(
                String.format("%s/%s", API_PLANETS, ID),
                jsonPlanet.write(planetDTOResponse).getJson(),
                status().isOk()).getContentAsString();

        assertEquals(json, jsonResponse.write(response).getJson());
    }

    @Test
    public void testDeletandoPlanet() throws Exception {
        PlanetDTO planetDTOResponse = PlanetDTO
                .builder()
                .name("Planet teste")
                .build();

        PlanetDTO planetDTORequest = PlanetDTO
                .builder()
                .id(ID)
                .name("Planet teste")
                .terrain(new ArrayList<Object>() {{
                    add("tundra");
                }})
                .climate(new ArrayList<Object>() {{
                    add("tropical");
                }})
                .films(3)
                .build();

        deleteHttpServletResponse(
                String.format("%s/%s", API_PLANETS, ID),
                null,
                status().isOk());
    }
}
