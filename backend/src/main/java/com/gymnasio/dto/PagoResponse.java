package com.gymnasio.dto;

import com.gymnasio.domain.model.Pago.EstadoPago;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PagoResponse(
        Integer id,
        Integer membresiaId, // ID de la membresía asociada (puede ser null)
        String metodoPago, // Solo el nombre del método
        BigDecimal monto,
        EstadoPago status,
        String referencia,
        OffsetDateTime fechaPago
) {
}