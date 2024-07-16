package com.logixs.estudiantes.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(EstudianteCursoPK.class)
public class EstudianteCurso {
    @Id
    private Long estudianteId;

    @Id
    private Long cursoId;
}
