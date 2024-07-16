package com.logixs.estudiantes.shared.mapper;

import com.logixs.estudiantes.domain.model.Estudiante;
import com.logixs.estudiantes.infrastructure.persistence.entity.EstudianteEntity;
import com.logixs.estudiantes.rest.dto.EstudianteRequestDTO;
import com.logixs.estudiantes.rest.dto.EstudianteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EstudianteMapper {

    public Estudiante toDomain(EstudianteRequestDTO estudianteRequestDTO) {
        if (estudianteRequestDTO == null) {
            return null;
        }
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(estudianteRequestDTO.getNombre());
        estudiante.setApellido(estudianteRequestDTO.getApellido());
        estudiante.setFechaNacimiento(estudianteRequestDTO.getFechaNacimiento());
        return estudiante;
    }

    public EstudianteResponseDTO toResponseDTO(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        EstudianteResponseDTO estudianteResponseDTO = new EstudianteResponseDTO();
        estudianteResponseDTO.setId(estudiante.getId());
        estudianteResponseDTO.setNombre(estudiante.getNombre());
        estudianteResponseDTO.setApellido(estudiante.getApellido());
        estudianteResponseDTO.setFechaNacimiento(estudiante.getFechaNacimiento());
        return estudianteResponseDTO;
    }

    public EstudianteEntity toEntity(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        EstudianteEntity estudianteEntity = new EstudianteEntity();
        estudianteEntity.setId(estudiante.getId());
        estudianteEntity.setNombre(estudiante.getNombre());
        estudianteEntity.setApellido(estudiante.getApellido());
        estudianteEntity.setFechaNacimiento(estudiante.getFechaNacimiento());
        return estudianteEntity;
    }

    public Estudiante toDomain(EstudianteEntity estudianteEntity) {
        if (estudianteEntity == null) {
            return null;
        }
        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteEntity.getId());
        estudiante.setNombre(estudianteEntity.getNombre());
        estudiante.setApellido(estudianteEntity.getApellido());
        estudiante.setFechaNacimiento(estudianteEntity.getFechaNacimiento());
        return estudiante;
    }
}
