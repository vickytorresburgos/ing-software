package edu.um.umbook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String titulo;

    private String contentType;
    private java.time.LocalDateTime fechaSubida;

    @ManyToOne
    private Album album;

    @OneToMany(mappedBy = "foto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    public Foto(String url, String titulo, String contentType, Album album) {
        this.url = url;
        this.titulo = titulo;
        this.contentType = contentType;
        this.album = album;
        this.fechaSubida = java.time.LocalDateTime.now();
    }
}
