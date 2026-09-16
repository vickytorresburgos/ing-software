package edu.um.umbook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@lombok.Setter
@NoArgsConstructor
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250)
    private String contenido;

    @ManyToOne
    private Usuario autor;

    @ManyToOne
    private Foto foto;

    @ManyToOne
    private Usuario muroDestino;

    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private ComentarioEstado estado = ComentarioEstado.CREADO;

    public Comentario(String contenido, Foto foto, Usuario autor) {
        this.contenido = contenido;
        this.foto = foto;
        this.autor = autor;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void modificarContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public void setEstado(ComentarioEstado estado) {
        this.estado = estado;
    }
}
