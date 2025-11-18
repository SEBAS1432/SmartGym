package com.gymnasio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Este 'record' define cómo enviaremos la info de un ejercicio
public record EjercicioRequest(
        @NotBlank @Size(max = 100)
        String ejercicio,
        
        Integer series,
        Integer repeticiones,
        Integer descansoSegundos
) {
}