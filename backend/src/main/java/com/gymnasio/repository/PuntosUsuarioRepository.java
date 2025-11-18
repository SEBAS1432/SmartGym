package com.gymnasio.repository;

import com.gymnasio.domain.model.PuntosUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Importa Param
import java.util.List;

public interface PuntosUsuarioRepository extends JpaRepository<PuntosUsuario, Integer> {
    
    // Historial de puntos (por si lo necesitas después)
    List<PuntosUsuario> findByUsuarioIdOrderByFechaDesc(Integer usuarioId);

    // Suma total de puntos para un usuario específico
    @Query("SELECT SUM(pu.puntos) FROM PuntosUsuario pu WHERE pu.usuario.id = :usuarioId")
    Integer sumPuntosByUsuarioId(@Param("usuarioId") Integer usuarioId);
}