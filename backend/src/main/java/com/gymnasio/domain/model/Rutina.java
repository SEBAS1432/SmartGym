package com.gymnasio.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet; // Importa HashSet
import java.util.Set;

@Entity
@Table(name = "rutinas")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String objetivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoRutina estado = EstadoRutina.ACTIVA; // Valor por defecto

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private OffsetDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RutinaEjercicio> ejercicios = new HashSet<>(); // Inicializa la colección

    public enum EstadoRutina {
        ACTIVA, PAUSADA, COMPLETADA
    }

    // --- Constructor Vacío (requerido por JPA) ---
    public Rutina() {
    }

    // --- Getters ---
    public Integer getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public EstadoRutina getEstado() {
        return estado;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public OffsetDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public Set<RutinaEjercicio> getEjercicios() {
        return ejercicios;
    }

    // --- Setters ---
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public void setEstado(EstadoRutina estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(OffsetDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public void setEjercicios(Set<RutinaEjercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    // --- Método útil para añadir ejercicios (opcional pero recomendado) ---
    public void addEjercicio(RutinaEjercicio ejercicio) {
        this.ejercicios.add(ejercicio);
        ejercicio.setRutina(this);
    }

    public void removeEjercicio(RutinaEjercicio ejercicio) {
        this.ejercicios.remove(ejercicio);
        ejercicio.setRutina(null);
    }

    // --- equals, hashCode, toString (Opcional, pero buena práctica) ---
    // Puedes generarlos con tu IDE si los necesitas
}