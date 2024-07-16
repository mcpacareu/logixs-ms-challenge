package com.logixs.cursos.rest.controller;

import com.logixs.cursos.rest.client.EstudianteClient;
import com.logixs.cursos.rest.dto.CursoRequestDTO;
import com.logixs.cursos.rest.dto.CursoResponseDTO;
import com.logixs.cursos.rest.dto.EstudianteResponseDTO;
import com.logixs.cursos.service.CursoService;
import com.logixs.cursos.shared.mapper.CursoMapper;
import com.logixs.cursos.domain.model.Curso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CursoControllerTest {

    @Mock
    private CursoService cursoService;

    @Mock
    private CursoMapper cursoMapper;

    @Mock
    private EstudianteClient estudianteClient;

    @InjectMocks
    private CursoController cursoController;

    private MockMvc mockMvc;

    private Curso curso;
    private CursoResponseDTO cursoResponseDTO;
    private CursoRequestDTO cursoRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();

        curso = new Curso(1L, "Curso de Java", "Descripción del curso", LocalDate.now(), LocalDate.now().plusDays(30));
        cursoResponseDTO = new CursoResponseDTO();
        cursoResponseDTO.setId(1L);
        cursoResponseDTO.setNombre("Curso de Java");
        cursoResponseDTO.setDescripcion("Descripción del curso");
        cursoResponseDTO.setFechaInicio(LocalDate.now());
        cursoResponseDTO.setFechaFin(LocalDate.now().plusDays(30));

        cursoRequestDTO = new CursoRequestDTO();
        cursoRequestDTO.setNombre("Curso de Java");
        cursoRequestDTO.setDescripcion("Descripción del curso");
        cursoRequestDTO.setFechaInicio(LocalDate.now());
        cursoRequestDTO.setFechaFin(LocalDate.now().plusDays(30));
    }

    @Test
    void listarCursos() throws Exception {
        when(cursoService.listarCursos()).thenReturn(Arrays.asList(curso));
        when(cursoMapper.toResponseDTO(curso)).thenReturn(cursoResponseDTO);

        mockMvc.perform(get("/cursos"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].nombre", is("Curso de Java")));
    }

    @Test
    void obtenerCursoPorId() throws Exception {
        when(cursoService.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));
        when(cursoMapper.toResponseDTO(curso)).thenReturn(cursoResponseDTO);

        mockMvc.perform(get("/cursos/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", is("Curso de Java")));
    }

    @Test
    void actualizarCurso() throws Exception {
        Curso
                cursoActualizado =
                new Curso(1L,
                          "Curso de Java Actualizado",
                          "Descripción actualizada",
                          LocalDate.parse("2022-01-01"),
                          LocalDate.parse("2022-01-31"));
        CursoResponseDTO cursoResponseActualizadoDTO = new CursoResponseDTO();
        cursoResponseActualizadoDTO.setId(1L);
        cursoResponseActualizadoDTO.setNombre("Curso de Java Actualizado");
        cursoResponseActualizadoDTO.setDescripcion("Descripción actualizada");
        cursoResponseActualizadoDTO.setFechaInicio(LocalDate.parse("2022-01-01"));
        cursoResponseActualizadoDTO.setFechaFin(LocalDate.parse("2022-01-31"));

        when(cursoMapper.toDomain(any(CursoRequestDTO.class))).thenReturn(cursoActualizado);
        when(cursoService.actualizarCurso(anyLong(), any(Curso.class))).thenReturn(cursoActualizado);
        when(cursoMapper.toResponseDTO(any(Curso.class))).thenReturn(cursoResponseActualizadoDTO);

        mockMvc.perform(put("/cursos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nombre\":\"Curso de Java Actualizado\",\"descripcion\":\"Descripción actualizada\",\"fechaInicio\":\"2022-01-01\",\"fechaFin\":\"2022-01-31\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", is("Curso de Java Actualizado")))
               .andExpect(jsonPath("$.descripcion", is("Descripción actualizada")));
    }

    @Test
    void guardarCurso() throws Exception {
        when(cursoMapper.toDomain(any(CursoRequestDTO.class))).thenReturn(curso);
        when(cursoService.guardarCurso(any(Curso.class))).thenReturn(curso);
        when(cursoMapper.toResponseDTO(any(Curso.class))).thenReturn(cursoResponseDTO);

        mockMvc.perform(post("/cursos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"id\":1,\"nombre\":\"Curso de Java\",\"descripcion\":\"Descripción del curso\",\"fechaInicio\":\"2022-01-01\",\"fechaFin\":\"2022-01-31\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.nombre", is("Curso de Java")));
    }

    @Test
    void eliminarCurso() throws Exception {
        mockMvc.perform(delete("/cursos/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    void inscribirEstudiante() throws Exception {
        when(cursoService.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));
        when(estudianteClient.obtenerEstudiantePorId(1L)).thenReturn(new EstudianteResponseDTO());
        when(estudianteClient.verificarInscripcion(1L, 1L)).thenReturn(false);

        mockMvc.perform(post("/cursos/1/inscribir/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Estudiante inscrito exitosamente"));
    }

    @Test
    void desinscribirEstudiante() throws Exception {
        when(cursoService.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));
        when(estudianteClient.obtenerEstudiantePorId(1L)).thenReturn(new EstudianteResponseDTO());
        when(estudianteClient.verificarInscripcion(1L, 1L)).thenReturn(true);

        mockMvc.perform(post("/cursos/1/desinscribir/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Estudiante desinscrito exitosamente"));
    }

    @Test
    void listarEstudiantesDeCurso() throws Exception {
        when(cursoService.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));
        when(estudianteClient.listarEstudiantesDeCurso(1L)).thenReturn(Arrays.asList(new EstudianteResponseDTO()));

        mockMvc.perform(get("/cursos/1/estudiantes"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void obtenerCursosPorIds() throws Exception {
        when(cursoService.obtenerCursosPorIds(anyList())).thenReturn(Arrays.asList(curso));
        when(cursoMapper.toResponseDTO(curso)).thenReturn(cursoResponseDTO);

        mockMvc.perform(post("/cursos/ids")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[1]"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].nombre", is("Curso de Java")));
    }
}
