package edu.um.umbook.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GrupoAmigos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "grupo_miembros",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "amigo_id"))
    private List<Amigo> miembros = new ArrayList<>();
    
    @OneToMany(mappedBy = "grupo")
    private List<Permiso> permisos = new ArrayList<>();
    
    public GrupoAmigos() {}

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return this.nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return this.descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Usuario getUsuario() { return this.usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public List<Amigo> getMiembros() { return this.miembros; }
    public void setMiembros(List<Amigo> miembros) { this.miembros = miembros; }
    
    public List<Permiso> getPermisos() { return this.permisos; }
    public void setPermisos(List<Permiso> permisos) { this.permisos = permisos; }
}
