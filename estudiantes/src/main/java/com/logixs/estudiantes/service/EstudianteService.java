package com.logixs.estudiantes.service;

import com.logixs.estudiantes.domain.model.Estudiante;
import com.logixs.estudiantes.domain.persistence.repository.EstudianteCursoJpaRepository;
import com.logixs.estudiantes.domain.persistence.repository.EstudianteRepository;
import com.logixs.estudiantes.exception.ResourceNotFoundException;
import com.logixs.estudiantes.infrastructure.persistence.entity.EstudianteCurso;
import com.logixs.estudiantes.shared.events.Inscripcion;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteCursoJpaRepository estudianteCursoJpaRepository;

    private static final String INSCRIBIR_ROUTING_KEY = "curso.estudiante.inscribir";
    private static final String DESINSCRIBIR_ROUTING_KEY = "curso.estudiante.desinscribir";
    private static final String ELIMINAR_CURSO_ROUTING_KEY = "curso.estudiante.eliminarCurso";
    private static final String COLA_CURSO_ESTUDIANTE = "curso-estudiante-queue";
    private static final String HEADER_ROUTING_KEY = "amqp_receivedRoutingKey";

    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository,
                             EstudianteCursoJpaRepository estudianteCursoJpaRepository) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteCursoJpaRepository = estudianteCursoJpaRepository;
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.listarEstudiantes();
    }

    public Optional<Estudiante> obtenerEstudiantePorId(Long id) {
        return estudianteRepository.obtenerEstudiantePorId(id);
    }

    @Transactional
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        return estudianteRepository.guardarEstudiante(estudiante);
    }

    @Transactional
    public Estudiante actualizarEstudiante(Long id, Estudiante estudianteNuevo) {
        return obtenerEstudiantePorId(id)
                .stream()
                .map(estudiante -> estudianteRepository.actualizarEstudiante(estudiante, estudianteNuevo))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
    }

    @Transactional
    public void eliminarEstudiante(Long id) {
        estudianteRepository.obtenerEstudiantePorId(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
        estudianteCursoJpaRepository.deleteByEstudianteId(id);
        estudianteRepository.eliminarEstudiante(id);
    }

    @Transactional
    @RabbitListener(queues = COLA_CURSO_ESTUDIANTE)
    public void recibirInscripcion(Inscripcion inscripcion, @Header(HEADER_ROUTING_KEY) String routingKey) {
        Long cursoId = inscripcion.getCursoId();
        Long estudianteId = inscripcion.getEstudianteId();

        switch (routingKey) {
            case INSCRIBIR_ROUTING_KEY:
                EstudianteCurso estudianteCurso = new EstudianteCurso(estudianteId, cursoId);
                estudianteCursoJpaRepository.save(estudianteCurso);
                break;

            case DESINSCRIBIR_ROUTING_KEY:
                estudianteCursoJpaRepository.deleteByEstudianteIdAndCursoId(estudianteId, cursoId);
                break;

            case ELIMINAR_CURSO_ROUTING_KEY:
                estudianteCursoJpaRepository.deleteByCursoId(cursoId);
                break;

            default:
                throw new IllegalArgumentException("Routing key no soportada: " + routingKey);
        }
    }

    public List<Long> listarCursosDeEstudiante(Long estudianteId) {
        return estudianteCursoJpaRepository.findCursoIdsByEstudianteId(estudianteId);
    }

    public List<Estudiante> listarEstudiantesDeCurso(Long cursoId) {
        List<Long> estudianteIds = estudianteCursoJpaRepository.findEstudianteIdsByCursoId(cursoId);
        return estudianteRepository.obtenerEstudiantesPorIds(estudianteIds);
    }

    public boolean verificarInscripcion(Long estudianteId, Long cursoId) {
        return estudianteCursoJpaRepository.existsByEstudianteIdAndCursoId(estudianteId, cursoId);
    }
}
