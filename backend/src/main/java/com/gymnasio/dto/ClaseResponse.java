package com.gymnasio.dto;

import com.gymnasio.domain.model.EstadoClase;

import java.time.OffsetDateTime;

public record ClaseResponse(
        Integer id,
        String titulo,
        String descripcion,
        String tipo,
        Integer instructorId,
        String instructorNombre,
        OffsetDateTime fechaInicio,
        Integer duracionMinutos,
        Integer capacidad,
        EstadoClase estado,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion) {
}
