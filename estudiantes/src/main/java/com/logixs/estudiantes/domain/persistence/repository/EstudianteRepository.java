package com.logixs.estudiantes.domain.persistence.repository;

import com.logixs.estudiantes.domain.model.Estudiante;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository {
    List<Estudiante> listarEstudiantes();

    Optional<Estudiante> obtenerEstudiantePorId(Long id);

    Estudiante guardarEstudiante(Estudiante estudiante);

    Estudiante actualizarEstudiante(Estudiante estudiante, Estudiante estudianteNuevo);

    void eliminarEstudiante(Long id);


    List<Estudiante> obtenerEstudiantesPorIds(List<Long> ids);
}
