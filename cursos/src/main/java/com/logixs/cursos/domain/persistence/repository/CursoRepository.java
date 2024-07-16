package com.logixs.cursos.domain.persistence.repository;

import com.logixs.cursos.domain.model.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoRepository {
    List<Curso> listarCursos();

    Optional<Curso> obtenerCursoPorId(Long id);

    Curso guardarCurso(Curso curso);

    Curso actualizarCurso(Curso curso, Curso cursoNuevo);

    void eliminarCurso(Long id);

    List<Curso> obtenerCursosPorIds(List<Long> ids);
}
