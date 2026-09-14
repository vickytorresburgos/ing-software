package edu.um.umbook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notificacion {
    public Notificacion() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;
    private boolean leida = false;
    private LocalDateTime fechaCreacion;
    
    @ManyToOne
    private Usuario destinatario;

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getMensaje() { return this.mensaje; }

    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isLeida() { return this.leida; }

    public void setLeida(boolean leida) { this.leida = leida; }

    public LocalDateTime getFechaCreacion() { return this.fechaCreacion; }

    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Usuario getDestinatario() { return this.destinatario; }

    public void setDestinatario(Usuario destinatario) { this.destinatario = destinatario; }

}
