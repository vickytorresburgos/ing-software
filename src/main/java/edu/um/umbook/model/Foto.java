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

    @Lob
    private byte[] contenido;

    private String contentType;

    @ManyToOne
    private Album album;

    @OneToMany(mappedBy = "foto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    public Foto(byte[] contenido, String contentType, Album album) {
        this.contenido = contenido;
        this.contentType = contentType;
        this.album = album;
    }
}
