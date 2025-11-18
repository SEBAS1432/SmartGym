package com.gymnasio.repository;

import com.gymnasio.domain.model.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {
    
    // Para buscar la membresía activa de un usuario
    Optional<Membresia> findByUsuarioIdAndEstado(Integer usuarioId, Membresia.EstadoMembresia estado);
    
    // Para el historial de membresías (si lo necesitaras después)
    List<Membresia> findByUsuarioIdOrderByFechaFinDesc(Integer usuarioId);
}