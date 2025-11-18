package com.gymnasio.service.impl;

import com.gymnasio.domain.model.EstadoReserva;
import com.gymnasio.domain.model.Rol;   
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.TrainerDashboardStats;
import com.gymnasio.repository.ClaseRepository;
import com.gymnasio.repository.ReservaRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.TrainerDashboardService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TrainerDashboardServiceImpl implements TrainerDashboardService {

    private final UsuarioRepository usuarioRepo;
    private final ClaseRepository claseRepo;
    private final ReservaRepository reservaRepo;

    public TrainerDashboardServiceImpl(UsuarioRepository usuarioRepo, 
                                       ClaseRepository claseRepo, 
                                       ReservaRepository reservaRepo) {
        this.usuarioRepo = usuarioRepo;
        this.claseRepo = claseRepo;
        this.reservaRepo = reservaRepo;
    }

    private Usuario getUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    @Override
    public TrainerDashboardStats getDashboardStats() {
        Usuario trainer = getUsuarioAutenticado();
        Integer trainerId = trainer.getId();
        
        // 1. Contar Clientes (total del sistema)
        long totalClientes = usuarioRepo.countByRol(Rol.CLIENTE);
        
        // 2. Contar Clases asignadas a este trainer
        long totalClases = claseRepo.countByInstructorId(trainerId);
        
        // 3. Contar Reservas activas para las clases de este trainer
        long totalReservas = reservaRepo.countByClaseInstructorIdAndEstado(trainerId, EstadoReserva.RESERVADA);
        String nombreTrainer = trainer.getNombres() + " " + trainer.getApellidos();

        return new TrainerDashboardStats(
            totalClientes,
            totalClases,
            totalReservas,
            nombreTrainer
        );
    }
}