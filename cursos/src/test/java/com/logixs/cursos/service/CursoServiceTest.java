package com.logixs.cursos.service;

import com.logixs.cursos.domain.model.Curso;
import com.logixs.cursos.domain.persistence.repository.CursoRepository;
import com.logixs.cursos.shared.events.Inscripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private Curso curso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        curso = new Curso(1L,
                          "Curso de Java",
                          "Descripción del curso",
                          LocalDate.now(),
                          LocalDate.now().plusDays(30));

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void listarCursos() {
        when(cursoRepository.listarCursos()).thenReturn(Arrays.asList(curso));
        List<Curso> cursos = cursoService.listarCursos();
        assertNotNull(cursos);
        assertEquals(1, cursos.size());
    }

    @Test
    void obtenerCursoPorId() {
        when(cursoRepository.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));
        Optional<Curso> cursoOpt = cursoService.obtenerCursoPorId(1L);
        assertTrue(cursoOpt.isPresent());
        assertEquals("Curso de Java", cursoOpt.get().getNombre());
    }

    @Test
    void guardarCurso() {
        when(cursoRepository.guardarCurso(curso)).thenReturn(curso);
        Curso cursoGuardado = cursoService.guardarCurso(curso);
        assertNotNull(cursoGuardado);
        assertEquals("Curso de Java", cursoGuardado.getNombre());
    }

    @Test
    void actualizarCurso() {
        Curso cursoActualizado = new Curso(1L, "Curso de Java Actualizado", "Descripción actualizada", LocalDate.now(), LocalDate.now().plusDays(30));
        when(cursoRepository.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.actualizarCurso(curso, cursoActualizado)).thenReturn(cursoActualizado);

        Curso cursoResult = cursoService.actualizarCurso(1L, cursoActualizado);
        assertNotNull(cursoResult);
        assertEquals("Curso de Java Actualizado", cursoResult.getNombre());
    }

    @Test
    void eliminarCurso() {
        doNothing().when(cursoRepository).eliminarCurso(1L);
        when(cursoRepository.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));

        cursoService.eliminarCurso(1L);

        verify(cursoRepository, times(1)).eliminarCurso(1L);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("curso-estudiante-exchange"), eq("curso.estudiante.eliminarCurso"), any(
                Inscripcion.class));
    }

    @Test
    void inscribirEstudiante() {
        Inscripcion inscripcion = new Inscripcion(1L, 1L);
        doNothing().when(rabbitTemplate).convertAndSend("curso-estudiante-exchange", "curso.estudiante.inscribir", inscripcion);

        cursoService.inscribirEstudiante(1L, 1L);

        verify(rabbitTemplate, times(1)).convertAndSend("curso-estudiante-exchange", "curso.estudiante.inscribir", inscripcion);
    }

    @Test
    void desinscribirEstudiante() {
        Inscripcion inscripcion = new Inscripcion(1L, 1L);
        doNothing().when(rabbitTemplate).convertAndSend("curso-estudiante-exchange", "curso.estudiante.desinscribir", inscripcion);

        cursoService.desinscribirEstudiante(1L, 1L);

        verify(rabbitTemplate, times(1)).convertAndSend("curso-estudiante-exchange", "curso.estudiante.desinscribir", inscripcion);
    }

    @Test
    void obtenerCursosPorIds() {
        when(cursoRepository.obtenerCursosPorIds(Arrays.asList(1L))).thenReturn(Arrays.asList(curso));
        List<Curso> cursos = cursoService.obtenerCursosPorIds(Arrays.asList(1L));
        assertNotNull(cursos);
        assertEquals(1, cursos.size());
        assertEquals("Curso de Java", cursos.get(0).getNombre());
    }
}
