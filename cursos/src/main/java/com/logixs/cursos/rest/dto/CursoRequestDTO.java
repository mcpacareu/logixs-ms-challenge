package com.logixs.cursos.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CursoRequestDTO {
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
