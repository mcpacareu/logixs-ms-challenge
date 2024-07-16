package com.logixs.estudiantes.service;

import com.logixs.estudiantes.domain.model.Estudiante;
import com.logixs.estudiantes.domain.persistence.repository.EstudianteCursoJpaRepository;
import com.logixs.estudiantes.domain.persistence.repository.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    @Mock
    private EstudianteCursoJpaRepository estudianteCursoJpaRepository;

    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        estudiante = new Estudiante(1L, "John", "Doe", LocalDate.now());
    }

    @Test
    void listarEstudiantes() {
        when(estudianteRepository.listarEstudiantes()).thenReturn(Arrays.asList(estudiante));
        List<Estudiante> estudiantes = estudianteService.listarEstudiantes();
        assertNotNull(estudiantes);
        assertEquals(1, estudiantes.size());
    }

    @Test
    void obtenerEstudiantePorId() {
        when(estudianteRepository.obtenerEstudiantePorId(1L)).thenReturn(Optional.of(estudiante));
        Optional<Estudiante> estudianteOpt = estudianteService.obtenerEstudiantePorId(1L);
        assertTrue(estudianteOpt.isPresent());
        assertEquals("John", estudianteOpt.get().getNombre());
    }

    @Test
    void guardarEstudiante() {
        when(estudianteRepository.guardarEstudiante(estudiante)).thenReturn(estudiante);
        Estudiante estudianteGuardado = estudianteService.guardarEstudiante(estudiante);
        assertNotNull(estudianteGuardado);
        assertEquals("John", estudianteGuardado.getNombre());
    }

    @Test
    void actualizarEstudiante() {
        Estudiante estudianteNuevo = new Estudiante(1L, "Jane", "Doe", LocalDate.now());
        when(estudianteRepository.obtenerEstudiantePorId(1L)).thenReturn(Optional.of(estudiante));
        when(estudianteRepository.actualizarEstudiante(estudiante, estudianteNuevo)).thenReturn(estudianteNuevo);

        Estudiante estudianteActualizado = estudianteService.actualizarEstudiante(1L, estudianteNuevo);
        assertNotNull(estudianteActualizado);
        assertEquals("Jane", estudianteActualizado.getNombre());
    }

    @Test
    void eliminarEstudiante() {
        doNothing().when(estudianteRepository).eliminarEstudiante(1L);
        when(estudianteRepository.obtenerEstudiantePorId(1L)).thenReturn(Optional.of(estudiante));


        estudianteService.eliminarEstudiante(1L);
        verify(estudianteRepository, times(1)).eliminarEstudiante(1L);
    }

    @Test
    void verificarInscripcion() {
        when(estudianteCursoJpaRepository.existsByEstudianteIdAndCursoId(1L, 1L)).thenReturn(true);

        boolean inscrito = estudianteService.verificarInscripcion(1L, 1L);
        assertTrue(inscrito);
    }
}
