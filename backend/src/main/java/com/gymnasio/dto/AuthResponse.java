package com.gymnasio.dto;

public record AuthResponse(
    String tipo, 
    String token, 
    String correo, 
    String rol, 
    String nombres, 
    String apellidos,
    Integer id
) {
}
