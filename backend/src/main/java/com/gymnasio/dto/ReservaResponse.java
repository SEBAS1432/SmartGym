package com.gymnasio.dto;

import com.gymnasio.domain.model.EstadoReserva;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ReservaResponse(
    Integer id,
    Integer claseId,
    String tituloClase,
    Integer usuarioId,
    String nombreUsuario,
    EstadoReserva estado,
    OffsetDateTime fechaReserva,
    OffsetDateTime fechaCreacion,
    OffsetDateTime fechaActualizacion,
    
    // Campos adicionales para mostrar en el frontend
    String instructorNombre,
    LocalDateTime fechaInicioClase,
    
    // Objeto anidado para facilitar el acceso en Angular
    ClaseInfo clase
) {
    // Constructor sin el objeto anidado (para crear desde el Service)
    public ReservaResponse(
        Integer id,
        Integer claseId,
        String tituloClase,
        Integer usuarioId,
        String nombreUsuario,
        EstadoReserva estado,
        OffsetDateTime fechaReserva,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion,
        String instructorNombre,
        LocalDateTime fechaInicioClase
    ) {
        this(
            id, claseId, tituloClase, usuarioId, nombreUsuario,
            estado, fechaReserva, fechaCreacion, fechaActualizacion,
            instructorNombre, fechaInicioClase,
            // Crear automáticamente el objeto anidado
            new ClaseInfo(claseId, tituloClase, instructorNombre, fechaInicioClase)
        );
    }
    
    // Record anidado para información de la clase
    public record ClaseInfo(
        Integer id,
        String titulo,
        String instructorNombre,
        LocalDateTime fechaInicio
    ) {}
}