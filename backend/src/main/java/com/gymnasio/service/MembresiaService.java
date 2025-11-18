package com.gymnasio.service;
import com.gymnasio.dto.MembresiaResponse;
import com.gymnasio.dto.MembresiaRequest;
import java.util.List;

public interface MembresiaService {
    /**
     * Obtiene la membresía activa del usuario autenticado.
     * Lanza una excepción si no se encuentra.
     */
    MembresiaResponse obtenerMembresiaActivaUsuarioAutenticado();

    MembresiaResponse crearMembresia(MembresiaRequest request);

    List<MembresiaResponse> listarPorUsuarioId(Integer usuarioId);
}