package com.gymnasio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

// Este 'record' define cómo crearemos o actualizaremos una rutina completa
public record RutinaRequest(
        @NotNull
        Integer usuarioId, // ¿Para qué usuario es esta rutina?

        @NotBlank @Size(max = 100)
        String nombre,

        @Size(max = 100)
        String objetivo,

        // Una lista de los ejercicios que irán dentro de esta rutina
        List<EjercicioRequest> ejercicios 
) {
}