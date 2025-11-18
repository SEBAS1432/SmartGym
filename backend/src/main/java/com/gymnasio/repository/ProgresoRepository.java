package com.gymnasio.repository;

import com.gymnasio.domain.model.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgresoRepository extends JpaRepository<Progreso, Integer> {
    
    // Para obtener el historial de un usuario, ordenado por fecha ascendente (para gráficos)
    List<Progreso> findByUsuarioIdOrderByFechaAsc(Integer usuarioId);
}