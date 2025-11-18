package com.gymnasio.dto;

import com.gymnasio.domain.model.Pago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para que el ADMIN registre un pago
public record PagoRequest(
        @NotNull
        Integer usuarioId, // ¿Quién pagó?

        Integer membresiaId, // ¿Qué membresía está pagando? (Opcional)

        @NotNull
        Integer metodoPagoId, // ¿Cómo pagó? (ID de la tabla metodos_pago)

        @NotNull
        @Positive
        BigDecimal monto,

        @NotNull
        Pago.EstadoPago status, // "COMPLETADO", "PENDIENTE", etc.

        String referencia, // Nro de voucher, Yape, etc.

        @NotNull
        LocalDateTime fechaPago // Fecha y hora del pago
) {
}