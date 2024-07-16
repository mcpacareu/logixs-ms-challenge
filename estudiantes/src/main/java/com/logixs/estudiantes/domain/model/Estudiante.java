package com.logixs.estudiantes.domain.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estudiante {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
