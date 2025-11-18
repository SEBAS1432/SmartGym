package com.gymnasio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent; // Para asegurar que la fecha no sea futura
import java.math.BigDecimal;
import java.time.LocalDate;

// DTO para que el usuario envíe un nuevo registro de progreso
public record ProgresoRequest(
        @NotNull @PastOrPresent LocalDate fecha, // Fecha obligatoria y no futura
        BigDecimal pesoKg,
        BigDecimal grasaCorporalPct,
        String notas
) {
}