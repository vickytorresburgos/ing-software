package edu.um.umbook.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Usuario {
    public Usuario() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private boolean enabled = true;
    private int diasNotificacionCumple = 7;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Amigo> amigos = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrupoAmigos> grupos = new ArrayList<>();
    
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albumes = new ArrayList<>();

    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return this.nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return this.apellido; }

    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }

    public boolean isEnabled() { return this.enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int getDiasNotificacionCumple() { return this.diasNotificacionCumple; }

    public void setDiasNotificacionCumple(int diasNotificacionCumple) { this.diasNotificacionCumple = diasNotificacionCumple; }

    public List<Amigo> getAmigos() { return this.amigos; }

    public void setAmigos(List<Amigo> amigos) { this.amigos = amigos; }

    public List<GrupoAmigos> getGrupos() { return this.grupos; }

    public void setGrupos(List<GrupoAmigos> grupos) { this.grupos = grupos; }

    public List<Album> getAlbumes() { return this.albumes; }

    public void setAlbumes(List<Album> albumes) { this.albumes = albumes; }

    public List<Notificacion> getNotificaciones() { return this.notificaciones; }

    public void setNotificaciones(List<Notificacion> notificaciones) { this.notificaciones = notificaciones; }

}
