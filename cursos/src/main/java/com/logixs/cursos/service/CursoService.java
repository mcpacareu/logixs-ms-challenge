package com.logixs.cursos.service;

import com.logixs.cursos.domain.model.Curso;
import com.logixs.cursos.domain.persistence.repository.CursoRepository;
import com.logixs.cursos.exception.ResourceNotFoundException;
import com.logixs.cursos.shared.events.Inscripcion;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private static final String EXCHANGE = "curso-estudiante-exchange";
    private static final String ROUTING_KEY_ELIMINAR_CURSO = "curso.estudiante.eliminarCurso";
    private static final String ROUTING_KEY_INSCRIBIR = "curso.estudiante.inscribir";
    private static final String ROUTING_KEY_DESINSCRIBIR = "curso.estudiante.desinscribir";


    private final CursoRepository cursoRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CursoService(CursoRepository cursoRepository,
                        RabbitTemplate rabbitTemplate) {
        this.cursoRepository = cursoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Curso> listarCursos() {
        return cursoRepository.listarCursos();
    }

    public Optional<Curso> obtenerCursoPorId(Long id) {
        return cursoRepository.obtenerCursoPorId(id);
    }

    @Transactional
    public Curso guardarCurso(Curso curso) {
        return cursoRepository.guardarCurso(curso);
    }

    @Transactional
    public Curso actualizarCurso(Long id, Curso cursoNuevo) {
        return obtenerCursoPorId(id)
                .stream()
                .map(curso -> cursoRepository.actualizarCurso(curso, cursoNuevo))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
    }

    @Transactional
    public void eliminarCurso(Long id) {
        cursoRepository.obtenerCursoPorId(id)
                       .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
        cursoRepository.eliminarCurso(id);

        Inscripcion inscripcion = new Inscripcion(id, null);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_ELIMINAR_CURSO, inscripcion);
    }

    public void inscribirEstudiante(Long cursoId, Long estudianteId) {
        Inscripcion inscripcion = new Inscripcion(cursoId, estudianteId);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_INSCRIBIR, inscripcion);
    }

    public void desinscribirEstudiante(Long cursoId, Long estudianteId) {
        Inscripcion inscripcion = new Inscripcion(cursoId, estudianteId);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_DESINSCRIBIR, inscripcion);
    }


    public List<Curso> obtenerCursosPorIds(List<Long> cursoIds) {
        return cursoRepository.obtenerCursosPorIds(cursoIds);
    }
}
