package com.gymnasio.dto;

// DTO para enviar las estadísticas al dashboard del Trainer
public record TrainerDashboardStats(
    long totalClientes,     // Total de clientes en el sistema
    long totalClases,       // Total de clases asignadas a este trainer
    long totalReservas,     // Total de reservas activas para sus clases
    String nombreTrainer    // El nombre del trainer logueado
) {
}