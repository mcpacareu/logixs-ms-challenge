package com.logixs.estudiantes.domain.persistence.repository;

import com.logixs.estudiantes.infrastructure.persistence.entity.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EstudianteJpaRepository extends JpaRepository<EstudianteEntity, Long> {

}
