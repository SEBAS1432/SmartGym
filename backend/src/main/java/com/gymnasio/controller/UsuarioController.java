package com.gymnasio.controller;

import com.gymnasio.domain.model.Rol;
import com.gymnasio.domain.model.EstadoUsuario;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.UsuarioRequest;
import com.gymnasio.dto.UsuarioResponse;
import com.gymnasio.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuario Controller", description = "Endpoints para usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // --- ENDPOINTS SOLO PARA ADMIN ---

    @Operation(summary = "Listar TODOS los usuarios (ADMIN)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede ver a todos (incluidos otros Admins/Trainers)
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Crear un nuevo usuario (ADMIN)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede crear usuarios (ej. crear un Trainer)
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Actualizar cualquier usuario (ADMIN o el propio usuario)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UsuarioResponse> actualizarCualquierUsuario(
        @PathVariable Integer id, 
        @Valid @RequestBody UsuarioRequest request,
        @AuthenticationPrincipal Usuario principal 
) {
    UsuarioResponse response = service.actualizar(id, request);
    return ResponseEntity.ok(response);
}

    @Operation(summary = "Eliminar (desactivar) un usuario (ADMIN)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
}

    @Operation(summary = "Activar un usuario (ADMIN)")
    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> activar(@PathVariable Integer id) {
        UsuarioResponse response = service.activar(id);
        return ResponseEntity.ok(response);
    }

    // --- ENDPOINTS PARA ADMIN Y TRAINER ---

    @Operation(summary = "Buscar usuarios por filtro (ADMIN/TRAINER)")
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','TRAINER')")
    public List<UsuarioResponse> buscar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Rol rol,
            @RequestParam(required = false) EstadoUsuario estado) {
        // Asegúrate de que tu UsuarioService tenga un método "buscar"
        return service.buscar(q, rol, estado); 
    }

    @Operation(summary = "Listar usuarios para selects de instructores (ADMIN/TRAINER)")
    @GetMapping("/lista-simple")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<UsuarioResponse>> listarParaSelects() {
        List<UsuarioResponse> instructores = service.listar().stream()
                .filter(u -> u.rol().name().equals("TRAINER") || u.rol().name().equals("ADMIN"))
                .toList();
        return ResponseEntity.ok(instructores);
    }

    @Operation(summary = "Listar todos los CLIENTES (ADMIN/TRAINER)")
    @GetMapping("/rol/CLIENTE")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<UsuarioResponse>> listarClientes() {
        List<UsuarioResponse> clientes = service.listar().stream()
                .filter(u -> u.rol().name().equals("CLIENTE"))
                .toList();
        return ResponseEntity.ok(clientes);
    }

    // --- ENDPOINT PARA "MI PERFIL" (CLIENTE) Y ADMIN/TRAINER ---

    @Operation(summary = "Obtener un usuario por id (Para 'Mi Perfil' o Admin/Trainer)")
    @GetMapping("/{id}")
    // ESTA ES LA SEGURIDAD IMPORTANTE:
    // Permite el acceso si eres ADMIN o TRAINER...
    // ... O si el ID que pides es TU PROPIO ID (para la página "Mi Perfil" del cliente)
    @PreAuthorize("hasAnyRole('ADMIN','TRAINER') or #id == principal.id") 
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Integer id, @AuthenticationPrincipal Usuario principal) {
        return ResponseEntity.ok(service.obtener(id));
    }
}