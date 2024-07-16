package com.logixs.estudiantes.rest.client;

import com.logixs.estudiantes.rest.dto.CursoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;

@FeignClient(value = "cursos",
             qualifiers = "cursos",
             url = "${cursos-service.url}",
             fallback = CursoClientFallback.class)
public interface CursoClient {

    @PostMapping("/cursos/ids")
    List<CursoResponseDTO> obtenerCursosPorIds(@RequestBody List<Long> cursoIds);
}

@Component
class CursoClientFallback implements CursoClient {

    @Override
    public List<CursoResponseDTO> obtenerCursosPorIds(List<Long> cursoIds) {
        return Collections.emptyList();
    }
}
