package com.gymnasio.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "clases")
public class Clase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  // En BD: varchar(100)
  @Column(nullable = false, length = 100)
  private String titulo;

  @Column(columnDefinition = "text")
  private String descripcion;

  @Column(length = 50)
  private String tipo; // Yoga, HIIT, etc.

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "instructor_id", nullable = false)
  private Usuario instructor;

  @Column(name = "fecha_inicio")
  private OffsetDateTime fechaInicio;

  @Column(name = "duracion_minutos")
  private Integer duracionMinutos;

  // NOT NULL (alineado a ALTER en BD)
  @Column(nullable = false)
  private Integer capacidad;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 12)
  private EstadoClase estado = EstadoClase.PROGRAMADA;

  @CreationTimestamp
  @Column(name = "fecha_creacion", updatable = false)
  private OffsetDateTime fechaCreacion;

  @UpdateTimestamp
  @Column(name = "fecha_actualizacion")
  private OffsetDateTime fechaActualizacion;
}
