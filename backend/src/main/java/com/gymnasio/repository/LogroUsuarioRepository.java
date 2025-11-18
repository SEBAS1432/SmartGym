package com.gymnasio.repository;

import com.gymnasio.domain.model.LogroUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Importa Param
import java.util.List;

public interface LogroUsuarioRepository extends JpaRepository<LogroUsuario, Integer> {
    
    // Obtenemos los logros de un usuario y traemos la info del logro (JOIN FETCH)
    // Usamos @Param para vincular el parámetro del método con la consulta JPQL
    @Query("SELECT lu FROM LogroUsuario lu JOIN FETCH lu.logro WHERE lu.usuario.id = :usuarioId")
    List<LogroUsuario> findByUsuarioIdWithLogro(@Param("usuarioId") Integer usuarioId);
}