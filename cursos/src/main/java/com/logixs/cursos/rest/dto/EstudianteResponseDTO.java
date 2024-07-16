package com.logixs.cursos.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EstudianteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
