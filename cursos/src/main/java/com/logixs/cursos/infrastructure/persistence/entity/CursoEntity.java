package com.logixs.cursos.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "curso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public void updateFrom(CursoEntity updatedCurso) {
        this.nombre = updatedCurso.getNombre();
        this.descripcion = updatedCurso.getDescripcion();
        this.fechaInicio = updatedCurso.getFechaInicio();
        this.fechaFin = updatedCurso.getFechaFin();
    }

}