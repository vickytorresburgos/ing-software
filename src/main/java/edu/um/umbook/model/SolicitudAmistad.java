package edu.um.umbook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SolicitudAmistad {
    public SolicitudAmistad() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Usuario solicitante;
    
    @ManyToOne
    private Usuario receptor;
    
    private String estado; // PENDING, ACCEPTED, REJECTED
    private LocalDateTime fechaCreacion;

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public Usuario getSolicitante() { return this.solicitante; }

    public void setSolicitante(Usuario solicitante) { this.solicitante = solicitante; }

    public Usuario getReceptor() { return this.receptor; }

    public void setReceptor(Usuario receptor) { this.receptor = receptor; }

    public String getEstado() { return this.estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return this.fechaCreacion; }

    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

}
