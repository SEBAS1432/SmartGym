package com.gymnasio.controller;

import com.gymnasio.dto.AuthResponse;
import com.gymnasio.dto.LoginRequest;
import com.gymnasio.dto.UsuarioRequest;
import com.gymnasio.dto.UsuarioResponse;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.security.JwtTokenProvider;
import com.gymnasio.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

  private final AuthenticationManager authManager;
  private final JwtTokenProvider jwt;
  private final UsuarioRepository repo;
  private final UsuarioService usuarioService;

  public AutenticacionController(
      AuthenticationManager authManager,
      JwtTokenProvider jwt,
      UsuarioRepository repo,
      UsuarioService usuarioService
  ) {
    this.authManager = authManager;
    this.jwt = jwt;
    this.repo = repo;
    this.usuarioService = usuarioService;
  }

  @PostMapping("/registro")
  public ResponseEntity<UsuarioResponse> registro(@Valid @RequestBody UsuarioRequest req) {
    // OJO: La contraseña se encripta en UsuarioServiceImpl con PasswordEncoder
    UsuarioResponse creado = usuarioService.crear(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
    try {
      // Valida credenciales (si son invalidas lanza BadCredentialsException)
      Authentication auth = authManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.correo(), req.contrasena())
      );

      // Busca al usuario (ya autenticado) para completar los datos del token/respuesta
      var user = repo.findByCorreo(req.correo()).orElseThrow();

      String token = jwt.generar(user.getCorreo(), user.getRol().name());

      return ResponseEntity.ok(
                new AuthResponse(
                        "Bearer",
                        token,
                        user.getCorreo(),
                        user.getRol().name(),
                        user.getNombres(),    // <--- Campo añadido
                        user.getApellidos(),   // <--- Campo añadido
                        user.getId()
                )
        );
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
