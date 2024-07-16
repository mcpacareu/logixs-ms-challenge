package com.logixs.estudiantes.infrastructure.persistence.repository;

import com.logixs.estudiantes.infrastructure.persistence.entity.EstudianteEntity;
import com.logixs.estudiantes.domain.persistence.repository.EstudianteJpaRepository;
import com.logixs.estudiantes.shared.mapper.EstudianteMapper;
import com.logixs.estudiantes.domain.model.Estudiante;
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

class EstudianteRepositoryJpaImplTest {

    @Mock
    private EstudianteJpaRepository estudianteJpaRepository;

    @Mock
    private EstudianteMapper estudianteMapper;

    @InjectMocks
    private EstudianteRepositoryJpaImpl estudianteRepositoryJpaImpl;

    private EstudianteEntity estudianteEntity;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        estudianteEntity = new EstudianteEntity(1L, "John", "Doe", LocalDate.now());
        estudiante = new Estudiante(1L, "John", "Doe", LocalDate.now());
    }

    @Test
    void listarEstudiantes() {
        when(estudianteJpaRepository.findAll()).thenReturn(Arrays.asList(estudianteEntity));
        when(estudianteMapper.toDomain(estudianteEntity)).thenReturn(estudiante);

        List<Estudiante> estudiantes = estudianteRepositoryJpaImpl.listarEstudiantes();
        assertNotNull(estudiantes);
        assertEquals(1, estudiantes.size());
    }

    @Test
    void obtenerEstudiantePorId() {
        when(estudianteJpaRepository.findById(1L)).thenReturn(Optional.of(estudianteEntity));
        when(estudianteMapper.toDomain(estudianteEntity)).thenReturn(estudiante);

        Optional<Estudiante> estudianteOpt = estudianteRepositoryJpaImpl.obtenerEstudiantePorId(1L);
        assertTrue(estudianteOpt.isPresent());
        assertEquals("John", estudianteOpt.get().getNombre());
    }

    @Test
    void guardarEstudiante() {
        when(estudianteJpaRepository.save(estudianteEntity)).thenReturn(estudianteEntity);
        when(estudianteMapper.toEntity(estudiante)).thenReturn(estudianteEntity);
        when(estudianteMapper.toDomain(estudianteEntity)).thenReturn(estudiante);

        Estudiante estudianteGuardado = estudianteRepositoryJpaImpl.guardarEstudiante(estudiante);
        assertNotNull(estudianteGuardado);
        assertEquals("John", estudianteGuardado.getNombre());
    }

    @Test
    void actualizarEstudiante() {
        EstudianteEntity estudianteNuevoEntity = new EstudianteEntity(1L, "Jane", "Doe", LocalDate.now());
        Estudiante estudianteNuevo = new Estudiante(1L, "Jane", "Doe", LocalDate.now());

        when(estudianteMapper.toEntity(estudiante)).thenReturn(estudianteEntity);
        when(estudianteMapper.toEntity(estudianteNuevo)).thenReturn(estudianteNuevoEntity);
        when(estudianteMapper.toDomain(estudianteNuevoEntity)).thenReturn(estudianteNuevo);
        when(estudianteJpaRepository.save(estudianteEntity)).thenReturn(estudianteNuevoEntity);

        Estudiante estudianteActualizado = estudianteRepositoryJpaImpl.actualizarEstudiante(estudiante, estudianteNuevo);
        assertNotNull(estudianteActualizado);
        assertEquals("Jane", estudianteActualizado.getNombre());
    }

    @Test
    void eliminarEstudiante() {
        doNothing().when(estudianteJpaRepository).deleteById(1L);
        estudianteRepositoryJpaImpl.eliminarEstudiante(1L);
        verify(estudianteJpaRepository, times(1)).deleteById(1L);
    }
}
