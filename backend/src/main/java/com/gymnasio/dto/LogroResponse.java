package com.gymnasio.dto;

import java.time.OffsetDateTime;

// DTO para mostrar un logro que el usuario ha ganado
public record LogroResponse(
        String nombre,
        String descripcion,
        Integer puntos,
        OffsetDateTime fechaObtenido // La fecha de la tabla logros_usuario
) {
}