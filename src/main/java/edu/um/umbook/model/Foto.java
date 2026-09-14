package edu.um.umbook.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Foto {
    public Foto() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    private byte[] contenido;
    private String contentType;
    private String comentarioOriginal;
    
    @ManyToOne
    private Album album;
    
    @OneToMany(mappedBy = "foto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public byte[] getContenido() { return this.contenido; }

    public void setContenido(byte[] contenido) { this.contenido = contenido; }

    public String getContentType() { return this.contentType; }

    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getComentarioOriginal() { return this.comentarioOriginal; }

    public void setComentarioOriginal(String comentarioOriginal) { this.comentarioOriginal = comentarioOriginal; }

    public Album getAlbum() { return this.album; }

    public void setAlbum(Album album) { this.album = album; }

    public List<Comentario> getComentarios() { return this.comentarios; }

    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }

}
