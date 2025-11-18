package com.gymnasio.dto;

import com.gymnasio.domain.model.Rutina.EstadoRutina;
import java.time.OffsetDateTime;
import java.util.List;

public record RutinaResponse(
        Integer id,
        Integer usuarioId,
        String nombre,
        String objetivo,
        EstadoRutina estado,
        OffsetDateTime fechaCreacion,
        List<EjercicioResponse> ejercicios
) {
}