package com.logixs.estudiantes.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteCursoPK implements Serializable {
    private Long estudianteId;
    private Long cursoId;
}
