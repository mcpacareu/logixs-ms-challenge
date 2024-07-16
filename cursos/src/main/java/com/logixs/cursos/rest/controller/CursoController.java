package com.logixs.cursos.rest.controller;

import com.logixs.cursos.exception.ResourceNotFoundException;
import com.logixs.cursos.rest.client.EstudianteClient;
import com.logixs.cursos.rest.dto.CursoRequestDTO;
import com.logixs.cursos.rest.dto.CursoResponseDTO;
import com.logixs.cursos.rest.dto.EstudianteResponseDTO;
import com.logixs.cursos.service.CursoService;
import com.logixs.cursos.shared.mapper.CursoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private final EstudianteClient estudianteClient;
    private final CursoService cursoService;
    private final CursoMapper cursoMapper;


    @Autowired
    public CursoController(@Qualifier("estudiantes") EstudianteClient estudianteClient,
                           CursoService cursoService,
                           CursoMapper cursoMapper) {
        this.estudianteClient = estudianteClient;
        this.cursoService = cursoService;
        this.cursoMapper = cursoMapper;
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> listarCursos() {
        List<CursoResponseDTO> cursos = cursoService.listarCursos().stream()
                                                    .map(cursoMapper::toResponseDTO)
                                                    .collect(Collectors.toList());

        return ResponseEntity.ok(cursos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> obtenerCursoPorId(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id)
                           .map(cursoMapper::toResponseDTO)
                           .map(ResponseEntity::ok)
                           .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> guardarCurso(@RequestBody CursoRequestDTO cursoRequestDTO) {
        return Optional.of(cursoRequestDTO)
                       .map(cursoMapper::toDomain)
                       .map(cursoService::guardarCurso)
                       .map(cursoMapper::toResponseDTO)
                       .map(responseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(responseDTO))
                       .orElseThrow(() -> new IllegalArgumentException("Error al guardar el curso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> actualizarCurso(@PathVariable Long id, @RequestBody CursoRequestDTO cursoRequestDTO) {
        return Optional.of(cursoRequestDTO)
                       .map(cursoMapper::toDomain)
                       .map(cursoNuevo -> cursoService.actualizarCurso(id, cursoNuevo))
                       .map(cursoMapper::toResponseDTO)
                       .map(ResponseEntity::ok)
                       .orElseThrow(() -> new IllegalArgumentException("Error al actualizar el curso"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cursoId}/inscribir/{estudianteId}")
    public ResponseEntity<String> inscribirEstudiante(@PathVariable Long cursoId, @PathVariable Long estudianteId) {
        boolean cursoExiste = cursoService.obtenerCursoPorId(cursoId).isPresent();

        if (!cursoExiste) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso no encontrado");
        }

        boolean estudianteExiste = isEstudiante(estudianteId);

        if (!estudianteExiste) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado");
        }

        boolean yaInscrito = estudianteClient.verificarInscripcion(estudianteId, cursoId);

        if (yaInscrito) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El estudiante ya está inscrito en el curso");
        }

        cursoService.inscribirEstudiante(cursoId, estudianteId);
        return ResponseEntity.ok("Estudiante inscrito exitosamente");
    }

    @PostMapping("/{cursoId}/desinscribir/{estudianteId}")
    public ResponseEntity<String> desinscribirEstudiante(@PathVariable Long cursoId, @PathVariable Long estudianteId) {
        boolean cursoExiste = cursoService.obtenerCursoPorId(cursoId).isPresent();

        if (!cursoExiste) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso no encontrado");
        }

        boolean estudianteExiste = isEstudiante(estudianteId);

        if (!estudianteExiste) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado");
        }

        boolean yaInscrito = estudianteClient.verificarInscripcion(estudianteId, cursoId);

        if (!yaInscrito) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El estudiante no está inscrito en el curso");
        }

        cursoService.desinscribirEstudiante(cursoId, estudianteId);
        return ResponseEntity.ok("Estudiante desinscrito exitosamente");
    }

    @GetMapping("/{cursoId}/estudiantes")
    public ResponseEntity<?> listarEstudiantesDeCurso(@PathVariable Long cursoId) {
        boolean cursoExiste = cursoService.obtenerCursoPorId(cursoId).isPresent();

        if (!cursoExiste) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Curso no encontrado con id: " + cursoId);
            response.put("estudiantes", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<EstudianteResponseDTO> estudiantes = estudianteClient.listarEstudiantesDeCurso(cursoId);

        return ResponseEntity.ok(estudiantes);
    }

    @PostMapping("/ids")
    public ResponseEntity<List<CursoResponseDTO>> obtenerCursosPorIds(@RequestBody List<Long> cursoIds) {
        List<CursoResponseDTO> cursos = cursoService.obtenerCursosPorIds(cursoIds).stream()
                                                    .map(cursoMapper::toResponseDTO)
                                                    .collect(Collectors.toList());
        return ResponseEntity.ok(cursos);
    }

    private boolean isEstudiante(Long estudianteId) {
        EstudianteResponseDTO response = estudianteClient.obtenerEstudiantePorId(estudianteId);
        return Optional.ofNullable(response).isPresent();
    }
}
