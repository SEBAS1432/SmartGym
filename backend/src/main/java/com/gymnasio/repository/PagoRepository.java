package com.gymnasio.repository;

import com.gymnasio.domain.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

        // Para el historial de pagos de un usuario, ordenado por fecha descendente
        List<Pago> findByUsuarioIdOrderByFechaPagoDesc(Integer usuarioId);

        long countByStatusAndFechaPagoBetween(com.gymnasio.domain.model.Pago.EstadoPago estado,
                        OffsetDateTime fechaInicio,
                        OffsetDateTime fechaFin);

        List<Pago> findByStatusAndFechaPagoBetween(com.gymnasio.domain.model.Pago.EstadoPago estado,
                        OffsetDateTime fechaInicio, OffsetDateTime fechaFin);

        @Query("""
                        SELECT SUM(p.monto)
                        FROM Pago p
                        WHERE p.status = :estado
                          AND p.fechaPago BETWEEN :fechaInicio AND :fechaFin
                        """)
        BigDecimal sumMontoByStatusAndFechaPagoBetween(@Param("estado") Pago.EstadoPago estado,
                        @Param("fechaInicio") OffsetDateTime fechaInicio,
                        @Param("fechaFin") OffsetDateTime fechaFin);
}