package edu.um.umbook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String nombre;

    @Column(length = 250)
    private String descripcion;

    @ManyToOne
    private Usuario creador;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "album_grupos_visualizacion", joinColumns = @JoinColumn(name = "album_id"), inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private List<GrupoAmigos> gruposConPermisoDeVisualizacion = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "album_grupos_comentario", joinColumns = @JoinColumn(name = "album_id"), inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private List<GrupoAmigos> gruposConPermisoDeComentario = new ArrayList<>();

    public Album(String nombre, String descripcion, Usuario creador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador = creador;
    }

    public void asignarPermisos(List<GrupoAmigos> gruposVisualizacion, List<GrupoAmigos> gruposComentario) {
        this.gruposConPermisoDeVisualizacion = new ArrayList<>(gruposVisualizacion);
        this.gruposConPermisoDeComentario = new ArrayList<>(gruposComentario);
    }
}
