package com.gymnasio.repository;

import com.gymnasio.domain.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RutinaRepository extends JpaRepository<Rutina, Integer> {
    
    @Query("SELECT DISTINCT r FROM Rutina r LEFT JOIN FETCH r.ejercicios WHERE r.usuario.id = :usuarioId")
    List<Rutina> findByUsuarioIdWithEjercicios(Integer usuarioId);
}