package com.logixs.cursos.infrastructure.persistence.repository;

import com.logixs.cursos.infrastructure.persistence.entity.CursoEntity;
import com.logixs.cursos.domain.persistence.repository.CursoJpaRepository;
import com.logixs.cursos.shared.mapper.CursoMapper;
import com.logixs.cursos.domain.model.Curso;
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

class CursoRepositoryJpaImplTest {

    @Mock
    private CursoJpaRepository cursoJpaRepository;

    @Mock
    private CursoMapper cursoMapper;

    @InjectMocks
    private CursoRepositoryJpaImpl cursoRepositoryJpaImpl;

    private CursoEntity cursoEntity;
    private Curso curso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cursoEntity = new CursoEntity(1L,
                                      "Curso de Java",
                                      "Descripción del curso",
                                      LocalDate.now(),
                                      LocalDate.now().plusDays(30));
        curso = new Curso(1L,
                          "Curso de Java",
                          "Descripción del curso",
                          LocalDate.now(),
                          LocalDate.now().plusDays(30));
    }

    @Test
    void listarCursos() {
        when(cursoJpaRepository.findAll()).thenReturn(Arrays.asList(cursoEntity));
        when(cursoMapper.toDomain(cursoEntity)).thenReturn(curso);

        List<Curso> cursos = cursoRepositoryJpaImpl.listarCursos();
        assertNotNull(cursos);
        assertEquals(1, cursos.size());
    }

    @Test
    void obtenerCursoPorId() {
        when(cursoJpaRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
        when(cursoMapper.toDomain(cursoEntity)).thenReturn(curso);

        Optional<Curso> cursoOpt = cursoRepositoryJpaImpl.obtenerCursoPorId(1L);
        assertTrue(cursoOpt.isPresent());
        assertEquals("Curso de Java", cursoOpt.get().getNombre());
    }

    @Test
    void guardarCurso() {
        when(cursoJpaRepository.save(cursoEntity)).thenReturn(cursoEntity);
        when(cursoMapper.toEntity(curso)).thenReturn(cursoEntity);
        when(cursoMapper.toDomain(cursoEntity)).thenReturn(curso);

        Curso cursoGuardado = cursoRepositoryJpaImpl.guardarCurso(curso);
        assertNotNull(cursoGuardado);
        assertEquals("Curso de Java", cursoGuardado.getNombre());
    }

    @Test
    void eliminarCurso() {
        doNothing().when(cursoJpaRepository).deleteById(1L);
        cursoRepositoryJpaImpl.eliminarCurso(1L);
        verify(cursoJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    void obtenerCursosPorIds() {
        when(cursoJpaRepository.findAllById(Arrays.asList(1L))).thenReturn(Arrays.asList(cursoEntity));
        when(cursoMapper.toDomain(cursoEntity)).thenReturn(curso);

        List<Curso> cursos = cursoRepositoryJpaImpl.obtenerCursosPorIds(Arrays.asList(1L));
        assertNotNull(cursos);
        assertEquals(1, cursos.size());
        assertEquals("Curso de Java", cursos.get(0).getNombre());
    }
}
