package edu.um.umbook.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Amigo {
    public Amigo() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "amigo_id")
    private Usuario amigoUsuario;

    private LocalDate fechaAmistad;

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return this.usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Usuario getAmigoUsuario() { return this.amigoUsuario; }

    public void setAmigoUsuario(Usuario amigoUsuario) { this.amigoUsuario = amigoUsuario; }

    public LocalDate getFechaAmistad() { return this.fechaAmistad; }

    public void setFechaAmistad(LocalDate fechaAmistad) { this.fechaAmistad = fechaAmistad; }

}
