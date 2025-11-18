-- =============================================================
-- PROYECTO: Gimnasio -  Spring Boot con MySQL
-- =============================================================

DROP DATABASE IF EXISTS `gymnasioBD`;
CREATE DATABASE `gymnasioBD`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `gymnasioBD`;

SET NAMES utf8mb4;
SET time_zone = '-05:00';

-- =========================
-- TABLA: usuarios
-- =========================
CREATE TABLE `usuarios` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(30) NOT NULL,
  `apellidos` VARCHAR(30) NOT NULL,
  `correo` VARCHAR(50) NOT NULL,
  `contrasena` VARCHAR(60) NOT NULL, -- pensado para BCrypt
  `telefono` VARCHAR(15) NULL,
  `rol` ENUM('ADMIN','TRAINER','CLIENTE') NOT NULL DEFAULT 'CLIENTE',
  `estado` ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuarios_correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: metodos_pago (catálogo)
-- =========================
CREATE TABLE `metodos_pago` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_metodo_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `metodos_pago` (`nombre`) VALUES ('EFECTIVO'),('TARJETA'),('YAPE/PLIN');

-- =========================
-- TABLA: clases
-- =========================
CREATE TABLE `clases` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(100) NOT NULL,
  `descripcion` TEXT NULL,
  `tipo` VARCHAR(50) NULL, -- p.ej. "Yoga", "HIIT", "Fuerza"
  `instructor_id` INT UNSIGNED NOT NULL,
  `fecha_inicio` DATETIME NULL,
  `duracion_minutos` INT NULL,
  `capacidad` INT NULL,
  `estado` ENUM('PROGRAMADA','CANCELADA','COMPLETADA') NOT NULL DEFAULT 'PROGRAMADA',
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_clases_fecha` (`fecha_inicio`),
  CONSTRAINT `fk_clases_instructor`
    FOREIGN KEY (`instructor_id`) REFERENCES `usuarios` (`id`)
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: reservas
-- =========================
CREATE TABLE `reservas` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_id` INT UNSIGNED NOT NULL,
  `clase_id` INT UNSIGNED NOT NULL,
  `estado` ENUM('RESERVADA','CANCELADA','ASISTIO','NO_ASISTIO') NOT NULL DEFAULT 'RESERVADA',
  `fecha_reserva` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_reserva_usuario_clase` (`usuario_id`,`clase_id`),
  KEY `idx_reservas_usuario` (`usuario_id`),
  KEY `idx_reservas_clase` (`clase_id`),
  CONSTRAINT `fk_reservas_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_reservas_clase`
    FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: membresias
-- =========================
CREATE TABLE `membresias` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_id` INT UNSIGNED NOT NULL,
  `tipo` VARCHAR(50) NULL,         -- p.ej. "Mensual", "Trimestral", "Anual"
  `fecha_inicio` DATE NULL,
  `fecha_fin` DATE NULL,
  `precio` DECIMAL(10,2) NULL,
  `estado` ENUM('ACTIVA','PAUSADA','EXPIRADA') NOT NULL DEFAULT 'ACTIVA',
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_membresias_usuario` (`usuario_id`),
  CONSTRAINT `fk_membresias_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: pagos
-- =========================
CREATE TABLE `pagos` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_id` INT UNSIGNED NOT NULL,
  `membresia_id` INT UNSIGNED NULL,
  `metodo_pago_id` INT UNSIGNED NOT NULL,
  `monto` DECIMAL(10,2) NOT NULL,
  `status` ENUM('PENDIENTE','COMPLETADO','FALLIDO') NOT NULL DEFAULT 'PENDIENTE',
  `referencia` VARCHAR(100) NULL,          -- código de operación, voucher, etc.
  `fecha_pago` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_pagos_usuario` (`usuario_id`),
  KEY `idx_pagos_fecha` (`fecha_pago`),
  CONSTRAINT `fk_pagos_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_pagos_membresia`
    FOREIGN KEY (`membresia_id`) REFERENCES `membresias` (`id`)
    ON DELETE SET NULL,
  CONSTRAINT `fk_pagos_metodo`
    FOREIGN KEY (`metodo_pago_id`) REFERENCES `metodos_pago` (`id`)
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: rutinas
-- =========================
CREATE TABLE `rutinas` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_id` INT UNSIGNED NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `objetivo` VARCHAR(100) NULL,  -- "Hipertrofia", "Bajar de peso", etc.
  `estado` ENUM('ACTIVA','PAUSADA','COMPLETADA') NOT NULL DEFAULT 'ACTIVA',
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rutinas_usuario` (`usuario_id`),
  CONSTRAINT `fk_rutinas_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: rutina_ejercicios
-- =========================
CREATE TABLE `rutina_ejercicios` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rutina_id` INT UNSIGNED NOT NULL,
  `ejercicio` VARCHAR(100) NOT NULL,
  `series` INT NULL,
  `repeticiones` INT NULL,
  `descanso_segundos` INT NULL,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rutina_ejercicios_rutina` (`rutina_id`),
  CONSTRAINT `fk_rutina_ejercicios_rutina`
    FOREIGN KEY (`rutina_id`) REFERENCES `rutinas` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: progreso
-- =========================
CREATE TABLE `progreso` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_id` INT UNSIGNED NOT NULL,
  `fecha` DATE NOT NULL,
  `peso_kg` DECIMAL(5,2) NULL,
  `grasa_corporal_pct` DECIMAL(5,2) NULL,
  `notas` TEXT NULL,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_progreso_usuario_fecha` (`usuario_id`,`fecha`),
  CONSTRAINT `fk_progreso_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: puntos_usuario
-- =========================
CREATE TABLE `puntos_usuario` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_id` INT UNSIGNED NOT NULL,
  `puntos` INT NOT NULL DEFAULT 0,
  `descripcion` VARCHAR(255) NULL,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_puntos_usuario_user_fecha` (`usuario_id`,`fecha`),
  CONSTRAINT `fk_puntos_usuario_user`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: logros (catálogo)
-- =========================
CREATE TABLE `logros` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` TEXT NULL,
  `puntos` INT NOT NULL DEFAULT 0,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_logros_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================
-- TABLA: logros_usuario (N:M entre usuarios y logros)
-- =========================
CREATE TABLE `logros_usuario` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_id` INT UNSIGNED NOT NULL,
  `logro_id` INT UNSIGNED NOT NULL,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_creacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_logro_por_usuario` (`usuario_id`, `logro_id`),
  KEY `idx_logros_usuario_logro` (`logro_id`),
  CONSTRAINT `fk_logros_usuario_user`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_logros_usuario_logro`
    FOREIGN KEY (`logro_id`) REFERENCES `logros` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
