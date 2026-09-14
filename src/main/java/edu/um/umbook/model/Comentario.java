package edu.um.umbook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comentario {
    public Comentario() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenido;
    
    @ManyToOne
    private Usuario autor;
    
    @ManyToOne
    private Foto foto; // null si es en el muro
    
    @ManyToOne
    private Usuario muroDestino; // null si es en una foto
    
    private LocalDateTime fechaCreacion;

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getContenido() { return this.contenido; }

    public void setContenido(String contenido) { this.contenido = contenido; }

    public Usuario getAutor() { return this.autor; }

    public void setAutor(Usuario autor) { this.autor = autor; }

    public Foto getFoto() { return this.foto; }

    public void setFoto(Foto foto) { this.foto = foto; }

    public Usuario getMuroDestino() { return this.muroDestino; }

    public void setMuroDestino(Usuario muroDestino) { this.muroDestino = muroDestino; }

    public LocalDateTime getFechaCreacion() { return this.fechaCreacion; }

    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

}
