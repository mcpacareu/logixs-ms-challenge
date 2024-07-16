package com.logixs.cursos.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.logixs.cursos.rest.client.EstudianteClient;
import com.logixs.cursos.rest.dto.EstudianteResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EstudianteClientIntegrationTest {

    @Qualifier("estudiantes")
    @Autowired
    private EstudianteClient estudianteClient;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("estudiantes-service.url", wireMockServer::baseUrl);
    }

    private static final String ESTUDIANTES_JSON_RESPONSE = """
                                                                [
                                                                    {
                                                                        "id": 1,
                                                                        "nombre": "John",
                                                                        "apellido": "Doe",
                                                                        "fechaNacimiento": "2000-01-01"
                                                                    }
                                                                ]
                                                            """;

    private static final String ESTUDIANTE_JSON_RESPONSE = """
                                                               {
                                                                   "id": 1,
                                                                   "nombre": "John",
                                                                   "apellido": "Doe",
                                                                   "fechaNacimiento": "2000-01-01"
                                                               }
                                                           """;

    @BeforeEach
    void setUp() {
        wireMockServer.stubFor(get(urlEqualTo("/estudiantes/curso/1"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody(ESTUDIANTES_JSON_RESPONSE)));

        wireMockServer.stubFor(get(urlEqualTo("/estudiantes/1"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody(ESTUDIANTE_JSON_RESPONSE)));

        wireMockServer.stubFor(get(urlEqualTo("/estudiantes/1/cursos/1/inscripcion"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody("true")));
    }

    @Test
    void listarEstudiantesDeCurso() {
        List<EstudianteResponseDTO> estudiantes = estudianteClient.listarEstudiantesDeCurso(1L);

        assertThat(estudiantes).hasSize(1);
        EstudianteResponseDTO estudiante = estudiantes.get(0);
        assertEquals("John", estudiante.getNombre());
        assertEquals("Doe", estudiante.getApellido());
        assertEquals("2000-01-01", estudiante.getFechaNacimiento().toString());
    }

    @Test
    void obtenerEstudiantePorId() {
        EstudianteResponseDTO estudiante = estudianteClient.obtenerEstudiantePorId(1L);

        assertEquals("John", estudiante.getNombre());
        assertEquals("Doe", estudiante.getApellido());
        assertEquals("2000-01-01", estudiante.getFechaNacimiento().toString());
    }

    @Test
    void verificarInscripcion() {
        boolean estaInscrito = estudianteClient.verificarInscripcion(1L, 1L);

        assertThat(estaInscrito).isTrue();
    }
}

