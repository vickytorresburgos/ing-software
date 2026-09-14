package edu.um.umbook.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GrupoAmigos {
    public GrupoAmigos() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "grupo_miembros",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "amigo_id"))
    private List<Amigo> miembros = new ArrayList<>();
    
    private boolean puedeVerAlbum = false;
    private boolean puedeComentarAlbum = false;
    private boolean puedeComentarMuro = false;

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return this.nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public Usuario getUsuario() { return this.usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Amigo> getMiembros() { return this.miembros; }

    public void setMiembros(List<Amigo> miembros) { this.miembros = miembros; }

    public boolean isPuedeVerAlbum() { return this.puedeVerAlbum; }

    public void setPuedeVerAlbum(boolean puedeVerAlbum) { this.puedeVerAlbum = puedeVerAlbum; }

    public boolean isPuedeComentarAlbum() { return this.puedeComentarAlbum; }

    public void setPuedeComentarAlbum(boolean puedeComentarAlbum) { this.puedeComentarAlbum = puedeComentarAlbum; }

    public boolean isPuedeComentarMuro() { return this.puedeComentarMuro; }

    public void setPuedeComentarMuro(boolean puedeComentarMuro) { this.puedeComentarMuro = puedeComentarMuro; }

}
