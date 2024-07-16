package com.logixs.cursos.infrastructure.persistence.repository;

import com.logixs.cursos.domain.model.Curso;
import com.logixs.cursos.domain.persistence.repository.CursoJpaRepository;
import com.logixs.cursos.domain.persistence.repository.CursoRepository;
import com.logixs.cursos.infrastructure.persistence.entity.CursoEntity;
import com.logixs.cursos.shared.mapper.CursoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CursoRepositoryJpaImpl implements CursoRepository {
    private final CursoJpaRepository jpaRepository;
    private final CursoMapper cursoMapper;

    @Autowired
    public CursoRepositoryJpaImpl(CursoJpaRepository jpaRepository, CursoMapper cursoMapper) {
        this.jpaRepository = jpaRepository;
        this.cursoMapper = cursoMapper;
    }

    @Override
    public List<Curso> listarCursos() {
        return jpaRepository.findAll().stream()
                            .map(cursoMapper::toDomain)
                            .collect(Collectors.toList());
    }

    @Override
    public Optional<Curso> obtenerCursoPorId(Long id) {
        return jpaRepository.findById(id)
                            .map(cursoMapper::toDomain);
    }

    @Override
    public Curso guardarCurso(Curso curso) {
        CursoEntity cursoEntity = cursoMapper.toEntity(curso);
        return cursoMapper.toDomain(jpaRepository.save(cursoEntity));
    }

    @Override
    public Curso actualizarCurso(Curso curso, Curso cursoNuevo) {
        CursoEntity cursoEntity = cursoMapper.toEntity(curso);
        CursoEntity cursoNuevoEntity = cursoMapper.toEntity(cursoNuevo);
        cursoEntity.updateFrom(cursoNuevoEntity);
        return cursoMapper.toDomain(jpaRepository.save(cursoEntity));
    }

    @Override
    public void eliminarCurso(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Curso> obtenerCursosPorIds(List<Long> ids) {
        return jpaRepository.findAllById(ids)
                            .stream()
                            .map(cursoMapper::toDomain)
                            .collect(Collectors.toList());
    }
}
