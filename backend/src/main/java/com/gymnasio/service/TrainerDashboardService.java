package com.gymnasio.service;

import com.gymnasio.dto.TrainerDashboardStats;

public interface TrainerDashboardService {
    /**
     * Obtiene las estadísticas agregadas para el dashboard
     * del TRAINER (o ADMIN) autenticado.
     */
    TrainerDashboardStats getDashboardStats();
}