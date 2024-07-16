package com.logixs.estudiantes.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EstudianteRequestDTO {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
