package com.gymnasio.service;
import com.gymnasio.dto.RutinaRequest;
import com.gymnasio.dto.RutinaResponse;
import java.util.List;

public interface RutinaService {
    List<RutinaResponse> listarPorUsuarioAutenticado();
    RutinaResponse obtenerPorId(Integer rutinaId);

    List<RutinaResponse> listarPorUsuarioId(Integer usuarioId); // <-- NUEVO
    RutinaResponse crear(RutinaRequest request); // <-- Modificado para recibir DTO
    RutinaResponse actualizar(Integer id, RutinaRequest request); // <-- Modificado
    void eliminar(Integer id); // <-- NUEVO (o modificar permisos)
}