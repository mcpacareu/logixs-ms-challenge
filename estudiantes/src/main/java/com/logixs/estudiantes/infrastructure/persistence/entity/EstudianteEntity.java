package com.logixs.estudiantes.infrastructure.persistence.entity;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "estudiante")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    public void updateFrom(EstudianteEntity updatedEstudiante) {
        this.nombre = updatedEstudiante.getNombre();
        this.apellido = updatedEstudiante.getApellido();
        this.fechaNacimiento = updatedEstudiante.getFechaNacimiento();
    }
}
