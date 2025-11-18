package com.gymnasio.security;

import com.gymnasio.domain.model.Usuario;
import com.gymnasio.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UsuarioRepository repo;
  public CustomUserDetailsService(UsuarioRepository repo){ this.repo = repo; }

  @Override
  public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
    Usuario u = repo.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("No existe usuario: "+correo));
    return new User(u.getCorreo(), u.getContrasena(), List.of(new SimpleGrantedAuthority("ROLE_"+u.getRol().name())));
  }
}
