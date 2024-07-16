package com.logixs.cursos.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CursoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
