package com.logixs.estudiantes.domain.persistence.repository;

import com.logixs.estudiantes.infrastructure.persistence.entity.EstudianteCurso;
import com.logixs.estudiantes.infrastructure.persistence.entity.EstudianteCursoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteCursoJpaRepository extends JpaRepository<EstudianteCurso, EstudianteCursoPK> {
    void deleteByEstudianteIdAndCursoId(Long estudianteId, Long cursoId);

    @Query("SELECT ec.cursoId FROM EstudianteCurso ec WHERE ec.estudianteId = :estudianteId")
    List<Long> findCursoIdsByEstudianteId(@Param("estudianteId") Long estudianteId);

    @Query("SELECT ec.estudianteId FROM EstudianteCurso ec WHERE ec.cursoId = :cursoId")
    List<Long> findEstudianteIdsByCursoId(@Param("cursoId") Long cursoId);

    boolean existsByEstudianteIdAndCursoId(Long estudianteId, Long cursoId);

    void deleteByEstudianteId(Long estudianteId);

    void deleteByCursoId(Long cursoId);
}
