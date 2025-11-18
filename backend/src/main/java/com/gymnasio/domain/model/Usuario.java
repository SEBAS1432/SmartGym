package com.gymnasio.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLCITextType;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(name = "uk_usuarios_correo", columnNames = "correo"))
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 50)
  private String nombres;

  @Column(nullable = false, length = 50)
  private String apellidos;

  //@Type(PostgreSQLCITextType.class)
  @Column(name = "correo", nullable = false, columnDefinition = "citext")
  private String correo;

  // BCrypt = 60 chars
  @Column(nullable = false, length = 60)
  private String contrasena;

  @Column(length = 20)
  private String telefono;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Rol rol = Rol.CLIENTE;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private EstadoUsuario estado = EstadoUsuario.ACTIVO;

  @CreationTimestamp
  @Column(name = "fecha_creacion", updatable = false)
  private OffsetDateTime fechaCreacion;

  @UpdateTimestamp
  @Column(name = "fecha_actualizacion")
  private OffsetDateTime fechaActualizacion;
}
