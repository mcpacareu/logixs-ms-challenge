package com.logixs.cursos.shared.mapper;

import com.logixs.cursos.domain.model.Curso;
import com.logixs.cursos.infrastructure.persistence.entity.CursoEntity;
import com.logixs.cursos.rest.dto.CursoRequestDTO;
import com.logixs.cursos.rest.dto.CursoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public Curso toDomain(CursoRequestDTO cursoRequestDTO) {
        if (cursoRequestDTO == null) {
            return null;
        }
        Curso curso = new Curso();
        curso.setNombre(cursoRequestDTO.getNombre());
        curso.setDescripcion(cursoRequestDTO.getDescripcion());
        curso.setFechaInicio(cursoRequestDTO.getFechaInicio());
        curso.setFechaFin(cursoRequestDTO.getFechaFin());
        return curso;
    }

    public CursoResponseDTO toResponseDTO(Curso curso) {
        if (curso == null) {
            return null;
        }
        CursoResponseDTO cursoResponseDTO = new CursoResponseDTO();
        cursoResponseDTO.setId(curso.getId());
        cursoResponseDTO.setNombre(curso.getNombre());
        cursoResponseDTO.setDescripcion(curso.getDescripcion());
        cursoResponseDTO.setFechaInicio(curso.getFechaInicio());
        cursoResponseDTO.setFechaFin(curso.getFechaFin());
        return cursoResponseDTO;
    }

    public CursoEntity toEntity(Curso curso) {
        if (curso == null) {
            return null;
        }
        CursoEntity cursoEntity = new CursoEntity();
        cursoEntity.setId(curso.getId());
        cursoEntity.setNombre(curso.getNombre());
        cursoEntity.setDescripcion(curso.getDescripcion());
        cursoEntity.setFechaInicio(curso.getFechaInicio());
        cursoEntity.setFechaFin(curso.getFechaFin());
        return cursoEntity;
    }

    public Curso toDomain(CursoEntity cursoEntity) {
        if (cursoEntity == null) {
            return null;
        }
        Curso curso = new Curso();
        curso.setId(cursoEntity.getId());
        curso.setNombre(cursoEntity.getNombre());
        curso.setDescripcion(cursoEntity.getDescripcion());
        curso.setFechaInicio(cursoEntity.getFechaInicio());
        curso.setFechaFin(cursoEntity.getFechaFin());
        return curso;
    }
}
