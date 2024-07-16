package com.logixs.estudiantes.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.logixs.estudiantes.rest.client.CursoClient;
import com.logixs.estudiantes.rest.dto.CursoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CursoClientIntegrationTest {

    @Qualifier("cursos")
    @Autowired
    private CursoClient cursoClient;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("cursos-service.url", wireMockServer::baseUrl);
    }

    private static final String CURSOS_JSON_RESPONSE = """
                                                           [
                                                               {
                                                                   "id": 1,
                                                                   "nombre": "Curso de prueba",
                                                                   "descripcion": "Descripción del curso de prueba",
                                                                   "fechaInicio": "2022-01-01",
                                                                   "fechaFin": "2022-01-20"
                                                               }
                                                           ]
                                                       """;

    @BeforeEach
    void setUp() {
        wireMockServer.stubFor(post(urlEqualTo("/cursos/ids"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody(CURSOS_JSON_RESPONSE)));

        wireMockServer.stubFor(get(urlEqualTo("/cursos/1"))
                                       .willReturn(aResponse()
                                                           .withStatus(400)
                                                           .withStatusMessage("Bad Request")));
    }

    @Test
    void obtenerCursosPorIds() {
        List<CursoResponseDTO> cursos = cursoClient.obtenerCursosPorIds(List.of(1L));

        assertEquals(1, cursos.size());
        CursoResponseDTO curso = cursos.get(0);
        assertEquals("Curso de prueba", curso.getNombre());
        assertEquals("Descripción del curso de prueba", curso.getDescripcion());
        assertEquals("2022-01-01", curso.getFechaInicio().toString());
        assertEquals("2022-01-20", curso.getFechaFin().toString());
    }

    @Test
    void testBadRequestResponse() throws Exception {
        URI uri = URI.create(wireMockServer.baseUrl() + "/cursos/1");
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");

        assertThat(connection.getResponseCode()).isEqualTo(400);
        assertThat(connection.getResponseMessage()).isEqualTo("Bad Request");
    }
}
