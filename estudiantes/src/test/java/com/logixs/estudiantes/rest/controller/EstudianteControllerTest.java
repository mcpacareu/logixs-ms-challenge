package com.logixs.estudiantes.rest.controller;

import com.logixs.estudiantes.rest.dto.EstudianteRequestDTO;
import com.logixs.estudiantes.rest.dto.EstudianteResponseDTO;
import com.logixs.estudiantes.service.EstudianteService;
import com.logixs.estudiantes.shared.mapper.EstudianteMapper;
import com.logixs.estudiantes.domain.model.Estudiante;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EstudianteControllerTest {

    @Mock
    private EstudianteService estudianteService;

    @Mock
    private EstudianteMapper estudianteMapper;

    @InjectMocks
    private EstudianteController estudianteController;

    private MockMvc mockMvc;

    private Estudiante estudiante;
    private EstudianteResponseDTO estudianteResponseDTO;
    private EstudianteRequestDTO estudianteRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(estudianteController).build();

        LocalDate now = LocalDate.now();
        estudiante = new Estudiante(1L, "John", "Doe", now);
        estudianteResponseDTO = new EstudianteResponseDTO();
        estudianteResponseDTO.setId(1L);
        estudianteResponseDTO.setNombre("John");
        estudianteResponseDTO.setApellido("Doe");
        estudianteResponseDTO.setFechaNacimiento(now);

        estudianteRequestDTO = new EstudianteRequestDTO();
        estudianteRequestDTO.setNombre("John");
        estudianteRequestDTO.setApellido("Doe");
        estudianteRequestDTO.setFechaNacimiento(now);
    }

    @Test
    void listarEstudiantes() throws Exception {
        when(estudianteService.listarEstudiantes()).thenReturn(Arrays.asList(estudiante));
        when(estudianteMapper.toResponseDTO(estudiante)).thenReturn(estudianteResponseDTO);

        mockMvc.perform(get("/estudiantes"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].nombre", is("John")));
    }

    @Test
    void obtenerEstudiantePorId() throws Exception {
        when(estudianteService.obtenerEstudiantePorId(1L)).thenReturn(Optional.of(estudiante));
        when(estudianteMapper.toResponseDTO(estudiante)).thenReturn(estudianteResponseDTO);

        mockMvc.perform(get("/estudiantes/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", is("John")));
    }

    @Test
    void guardarEstudiante() throws Exception {
        when(estudianteMapper.toDomain(any(EstudianteRequestDTO.class))).thenReturn(estudiante);
        when(estudianteService.guardarEstudiante(any(Estudiante.class))).thenReturn(estudiante);
        when(estudianteMapper.toResponseDTO(any(Estudiante.class))).thenReturn(estudianteResponseDTO);

        mockMvc.perform(post("/estudiantes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":1,\"nombre\":\"John\",\"apellido\":\"Doe\",\"fechaNacimiento\":\"2022-01-01\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.nombre", is("John")));
    }

    @Test
    void actualizarEstudiante() throws Exception {
        when(estudianteMapper.toDomain(any(EstudianteRequestDTO.class))).thenReturn(estudiante);
        when(estudianteService.actualizarEstudiante(eq(1L), any(Estudiante.class))).thenReturn(estudiante);
        when(estudianteMapper.toResponseDTO(any(Estudiante.class))).thenReturn(estudianteResponseDTO);

        mockMvc.perform(put("/estudiantes/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nombre\":\"John\",\"apellido\":\"Doe\",\"fechaNacimiento\":\"2022-01-01\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", is("John")));
    }

    @Test
    void eliminarEstudiante() throws Exception {
        mockMvc.perform(delete("/estudiantes/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    void verificarInscripcion() throws Exception {
        when(estudianteService.verificarInscripcion(1L, 1L)).thenReturn(true);

        mockMvc.perform(get("/estudiantes/1/cursos/1/inscripcion"))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
    }
}
