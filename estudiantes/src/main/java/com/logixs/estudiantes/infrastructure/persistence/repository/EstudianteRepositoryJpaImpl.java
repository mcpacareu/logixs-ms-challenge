package com.logixs.estudiantes.infrastructure.persistence.repository;

import com.logixs.estudiantes.domain.model.Estudiante;
import com.logixs.estudiantes.domain.persistence.repository.EstudianteRepository;
import com.logixs.estudiantes.infrastructure.persistence.entity.EstudianteEntity;
import com.logixs.estudiantes.shared.mapper.EstudianteMapper;
import com.logixs.estudiantes.domain.persistence.repository.EstudianteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EstudianteRepositoryJpaImpl implements EstudianteRepository {

    private final EstudianteJpaRepository jpaRepository;
    private final EstudianteMapper estudianteMapper;

    @Autowired
    public EstudianteRepositoryJpaImpl(EstudianteJpaRepository jpaRepository, EstudianteMapper estudianteMapper) {
        this.jpaRepository = jpaRepository;
        this.estudianteMapper = estudianteMapper;
    }

    @Override
    public List<Estudiante> listarEstudiantes() {
        return jpaRepository.findAll().stream()
                            .map(estudianteMapper::toDomain)
                            .collect(Collectors.toList());
    }

    @Override
    public Optional<Estudiante> obtenerEstudiantePorId(Long id) {
        return jpaRepository.findById(id).map(estudianteMapper::toDomain);
    }

    @Override
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        EstudianteEntity estudianteEntity = estudianteMapper.toEntity(estudiante);
        return estudianteMapper.toDomain(jpaRepository.save(estudianteEntity));
    }

    @Override
    public Estudiante actualizarEstudiante(Estudiante estudiante, Estudiante estudianteNuevo) {
        EstudianteEntity estudianteEntity = estudianteMapper.toEntity(estudiante);
        EstudianteEntity estudianteNuevoEntity = estudianteMapper.toEntity(estudianteNuevo);
        estudianteEntity.updateFrom(estudianteNuevoEntity);
        return estudianteMapper.toDomain(jpaRepository.save(estudianteEntity));
    }

    @Override
    public void eliminarEstudiante(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Estudiante> obtenerEstudiantesPorIds(List<Long> ids) {
        return jpaRepository.findAllById(ids).stream()
                            .map(estudianteMapper::toDomain)
                            .collect(Collectors.toList());
    }

}
