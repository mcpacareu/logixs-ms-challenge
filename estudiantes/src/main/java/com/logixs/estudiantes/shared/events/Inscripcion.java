package com.logixs.estudiantes.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion implements Serializable {
    private Long cursoId;
    private Long estudianteId;
}
