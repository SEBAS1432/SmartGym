package com.gymnasio.dto;

import com.gymnasio.domain.model.Membresia;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

// DTO para que el ADMIN cree o actualice una membresía
public record MembresiaRequest(
        @NotNull
        Integer usuarioId, // ¿A qué usuario se le asigna?

        @NotNull
        String tipo, // "Mensual", "Trimestral", etc.

        @NotNull
        LocalDate fechaInicio,

        @NotNull
        @Future // La fecha de fin debe ser en el futuro
        LocalDate fechaFin,

        @NotNull
        @Positive // El precio debe ser positivo
        BigDecimal precio,

        @NotNull
        Membresia.EstadoMembresia estado // "ACTIVA", "PAUSADA"
) {
}