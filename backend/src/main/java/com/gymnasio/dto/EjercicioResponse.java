package com.gymnasio.dto;

public record EjercicioResponse(
        Integer id,
        String ejercicio,
        Integer series,
        Integer repeticiones,
        Integer descansoSegundos
) {
}