package com.logixs.cursos.domain.persistence.repository;

import com.logixs.cursos.infrastructure.persistence.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoJpaRepository extends JpaRepository<CursoEntity, Long> {
}
