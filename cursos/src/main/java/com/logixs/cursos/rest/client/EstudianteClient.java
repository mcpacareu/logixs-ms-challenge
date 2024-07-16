package com.logixs.cursos.rest.client;

import com.logixs.cursos.rest.dto.EstudianteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(value = "estudiantes",
             qualifiers = "estudiantes",
             url = "${estudiantes-service.url}",
             fallback = EstudianteClientFallback.class)
public interface EstudianteClient {

    @GetMapping("/estudiantes/curso/{cursoId}")
    List<EstudianteResponseDTO> listarEstudiantesDeCurso(@PathVariable("cursoId") Long cursoId);

    @GetMapping("/estudiantes/{id}")
    EstudianteResponseDTO obtenerEstudiantePorId(@PathVariable Long id);

    @GetMapping("/estudiantes/{estudianteId}/cursos/{cursoId}/inscripcion")
    boolean verificarInscripcion(@PathVariable Long estudianteId, @PathVariable Long cursoId);
}

@Component
class EstudianteClientFallback implements EstudianteClient {

    @Override
    public List<EstudianteResponseDTO> listarEstudiantesDeCurso(Long cursoId) {
        return Collections.emptyList();
    }

    @Override
    public EstudianteResponseDTO obtenerEstudiantePorId(Long id) {
        return null;
    }

    @Override
    public boolean verificarInscripcion(Long estudianteId, Long cursoId) {
        return false;
    }
}