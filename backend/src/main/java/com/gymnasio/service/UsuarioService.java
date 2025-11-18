package com.gymnasio.service;

import com.gymnasio.domain.model.EstadoUsuario;
import com.gymnasio.domain.model.Rol;
import com.gymnasio.dto.UsuarioRequest;
import com.gymnasio.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
  List<UsuarioResponse> listar();
  UsuarioResponse obtener(Integer id);
  UsuarioResponse crear(UsuarioRequest request);
  UsuarioResponse actualizar(Integer id, UsuarioRequest request);
  List<UsuarioResponse> buscar(String q, Rol rol, EstadoUsuario estado);

  void eliminar(Integer id);

  UsuarioResponse activar(Integer id);
}

