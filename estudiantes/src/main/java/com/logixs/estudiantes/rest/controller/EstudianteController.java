package com.logixs.estudiantes.rest.controller;

import com.logixs.estudiantes.exception.ResourceNotFoundException;
import com.logixs.estudiantes.rest.client.CursoClient;
import com.logixs.estudiantes.rest.dto.CursoResponseDTO;
import com.logixs.estudiantes.rest.dto.EstudianteRequestDTO;
import com.logixs.estudiantes.rest.dto.EstudianteResponseDTO;
import com.logixs.estudiantes.service.EstudianteService;
import com.logixs.estudiantes.shared.mapper.EstudianteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final CursoClient cursoClient;
    private final EstudianteService estudianteService;
    private final EstudianteMapper estudianteMapper;

    @Autowired
    public EstudianteController(@Qualifier("cursos") CursoClient cursoClient,
                                EstudianteService estudianteService,
                                EstudianteMapper estudianteMapper) {
        this.cursoClient = cursoClient;
        this.estudianteService = estudianteService;
        this.estudianteMapper = estudianteMapper;
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> listarEstudiantes() {
        List<EstudianteResponseDTO> estudiantes = estudianteService.listarEstudiantes().stream()
                                                                   .map(estudianteMapper::toResponseDTO)
                                                                   .collect(Collectors.toList());
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorId(@PathVariable Long id) {
        return estudianteService.obtenerEstudiantePorId(id)
                                .map(estudianteMapper::toResponseDTO)
                                .map(ResponseEntity::ok)
                                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> guardarEstudiante(@RequestBody EstudianteRequestDTO estudianteRequestDTO) {
        return Optional.of(estudianteRequestDTO)
                       .map(estudianteMapper::toDomain)
                       .map(estudianteService::guardarEstudiante)
                       .map(estudianteMapper::toResponseDTO)
                       .map(responseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(responseDTO))
                       .orElseThrow(() -> new IllegalArgumentException("Error al guardar el estudiante"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> actualizarEstudiante(@PathVariable Long id,
                                                                      @RequestBody EstudianteRequestDTO estudianteRequestDTO) {
        return Optional.of(estudianteRequestDTO)
                       .map(estudianteMapper::toDomain)
                       .map(estudianteNuevo -> estudianteService.actualizarEstudiante(id, estudianteNuevo))
                       .map(estudianteMapper::toResponseDTO)
                       .map(ResponseEntity::ok)
                       .orElseThrow(() -> new IllegalArgumentException("Error al actualizar el estudiante"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{estudianteId}/cursos/{cursoId}/inscripcion")
    public ResponseEntity<Boolean> verificarInscripcion(@PathVariable Long estudianteId, @PathVariable Long cursoId) {
        boolean inscrito = estudianteService.verificarInscripcion(estudianteId, cursoId);
        return ResponseEntity.ok(inscrito);
    }

    @GetMapping("/{estudianteId}/cursos")
    public ResponseEntity<List<CursoResponseDTO>> listarCursosDeEstudiante(@PathVariable Long estudianteId) {
        estudianteService.obtenerEstudiantePorId(estudianteId)
                         .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " +
                                                                          estudianteId));
        List<Long> cursoIds = estudianteService.listarCursosDeEstudiante(estudianteId);
        return ResponseEntity.ok(cursoClient.obtenerCursosPorIds(cursoIds));
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<EstudianteResponseDTO>> listarEstudiantesDeCurso(@PathVariable Long cursoId) {
        List<EstudianteResponseDTO> estudiantesDTO = estudianteService.listarEstudiantesDeCurso(cursoId)
                                                                      .stream()
                                                                      .map(estudianteMapper::toResponseDTO)
                                                                      .collect(Collectors.toList());
        return ResponseEntity.ok(estudiantesDTO);
    }


}
