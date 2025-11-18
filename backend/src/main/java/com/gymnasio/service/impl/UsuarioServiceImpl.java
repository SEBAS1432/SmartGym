package com.gymnasio.service.impl;

import com.gymnasio.domain.model.EstadoUsuario;
import com.gymnasio.domain.model.Rol;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.UsuarioRequest;
import com.gymnasio.dto.UsuarioResponse;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

  private final UsuarioRepository repo;
  private final PasswordEncoder encoder;

  public UsuarioServiceImpl(UsuarioRepository repo, PasswordEncoder encoder) {
    this.repo = repo;
    this.encoder = encoder;
  }

  @Override
  public UsuarioResponse crear(UsuarioRequest r) {
    final String correo = r.correo() == null ? null : r.correo().trim();
    if (correo == null || correo.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo es obligatorio");
    }

    // ✅ CITEXT hará la comparación case-insensitive en la BD
    if (repo.existsByCorreo(correo)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
    }

    Usuario u = Usuario.builder()
        .nombres(r.nombres())
        .apellidos(r.apellidos())
        .correo(correo)
        .contrasena(encoder.encode(r.contrasena()))
        .telefono(r.telefono())
        .rol(r.rol() != null ? r.rol() : Rol.CLIENTE)
        .estado(r.estado() != null ? r.estado() : EstadoUsuario.ACTIVO)
        .build();

    u = repo.save(u);
    return toResponse(u);
  }

  @Override
  @Transactional(readOnly = true)
  public UsuarioResponse obtener(Integer id) {
    Usuario u = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    return toResponse(u);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UsuarioResponse> listar() {
    return repo.findAll().stream().map(this::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UsuarioResponse> buscar(String q, Rol rol, EstadoUsuario estado) {
    return repo.buscar(q, rol, estado).stream().map(this::toResponse).toList();
  }

  @Override
  public UsuarioResponse actualizar(Integer id, UsuarioRequest r) {
    Usuario u = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    final String nuevoCorreo = r.correo() == null ? null : r.correo().trim();
    if (nuevoCorreo == null || nuevoCorreo.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo es obligatorio");
    }

    if (!u.getCorreo().equals(nuevoCorreo) && repo.existsByCorreo(nuevoCorreo)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
    }

    u.setNombres(r.nombres());
    u.setApellidos(r.apellidos());
    u.setCorreo(nuevoCorreo);

    if (r.contrasena() != null && !r.contrasena().isBlank()) {
      u.setContrasena(encoder.encode(r.contrasena()));
    }

    u.setTelefono(r.telefono());
    if (r.rol() != null)
      u.setRol(r.rol());
    if (r.estado() != null)
      u.setEstado(r.estado());

    return toResponse(u);
  }

  @Override
  @Transactional
  public void eliminar(Integer id) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String correoAutenticado = auth.getName();

    Usuario usuarioAEliminar = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    if (usuarioAEliminar.getCorreo().equals(correoAutenticado)) {
      // Si son iguales, lanzamos un error
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Un administrador no puede desactivar su propia cuenta.");
    }
    usuarioAEliminar.setEstado(EstadoUsuario.INACTIVO);
    repo.save(usuarioAEliminar);
  }

  @Override
  @Transactional
  public UsuarioResponse activar(Integer id) {
    Usuario usuario = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    usuario.setEstado(EstadoUsuario.ACTIVO);
    Usuario usuarioGuardado = repo.save(usuario);

    return toResponse(usuarioGuardado);
  }

  private UsuarioResponse toResponse(Usuario u) {
    return new UsuarioResponse(
        u.getId(),
        u.getNombres(),
        u.getApellidos(),
        u.getCorreo(),
        u.getTelefono(),
        u.getRol(),
        u.getEstado(),
        u.getFechaCreacion(),
        u.getFechaActualizacion());
  }
}
