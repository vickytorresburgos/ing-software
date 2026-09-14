package edu.um.umbook.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {
    public Album() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    
    @ManyToOne
    private Usuario creador;
    
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(name = "album_grupos",
        joinColumns = @JoinColumn(name = "album_id"),
        inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private List<GrupoAmigos> gruposPermitidos = new ArrayList<>();

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return this.nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return this.descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Usuario getCreador() { return this.creador; }

    public void setCreador(Usuario creador) { this.creador = creador; }

    public List<Foto> getFotos() { return this.fotos; }

    public void setFotos(List<Foto> fotos) { this.fotos = fotos; }

    public List<GrupoAmigos> getGruposPermitidos() { return this.gruposPermitidos; }

    public void setGruposPermitidos(List<GrupoAmigos> gruposPermitidos) { this.gruposPermitidos = gruposPermitidos; }

}
