package com.gymnasio.service;

import com.gymnasio.dto.ClaseRequest;
import com.gymnasio.dto.ClaseResponse;
import com.gymnasio.dto.DisponibilidadClaseView;

import java.util.List;

public interface ClaseService {
    List<ClaseResponse> listar();
    ClaseResponse obtener(Integer id);
    DisponibilidadClaseView disponibilidad(Integer id);
    ClaseResponse crear(ClaseRequest request);
    ClaseResponse actualizar(Integer id, ClaseRequest request);

    void eliminar(Integer id);
}
