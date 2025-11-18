package com.gymnasio.repository;

import com.gymnasio.domain.model.EstadoReserva;
import com.gymnasio.domain.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    boolean existsByUsuario_IdAndClase_IdAndEstado(Integer usuarioId, Integer claseId, EstadoReserva estado);

    @Query("select count(r) from Reserva r where r.clase.id = :claseId and r.estado = :estado")
    long countByClaseIdAndEstado(Integer claseId, EstadoReserva estado);
    
    // IMPORTANTE: Query con JOIN FETCH para cargar todas las relaciones necesarias
    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.clase c " +
           "JOIN FETCH c.instructor i " +
           "JOIN FETCH r.usuario u " +
           "WHERE r.usuario.id = :usuarioId " +
           "ORDER BY c.fechaInicio DESC")
    List<Reserva> findReservasDetalladasPorUsuario(@Param("usuarioId") Integer usuarioId);
    
    // Query para asistentes de clase
    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.clase c " +
           "JOIN FETCH c.instructor i " +
           "JOIN FETCH r.usuario u " +
           "WHERE r.clase.id = :claseId AND r.estado = :estado " +
           "ORDER BY r.fechaReserva DESC")
    List<Reserva> findReservasDetalladasPorClaseId(
        @Param("claseId") Integer claseId, 
        @Param("estado") EstadoReserva estado
    );
    
    long countByClaseInstructorIdAndEstado(Integer instructorId, EstadoReserva estado);
    long countByFechaReservaBetween(OffsetDateTime fechaInicio, OffsetDateTime fechaFin);
}