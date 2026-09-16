package edu.um.umbook.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import edu.um.umbook.pattern.observer.Subject;
import edu.um.umbook.pattern.observer.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

@Entity
public class Usuario implements Subject {
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
    private String rol = "ROLE_USER";
    @Enumerated(EnumType.STRING)
    private UsuarioEstado estado = UsuarioEstado.ACTIVO;
    private int diasNotificacionCumple = 7;
    private LocalDate fechaNacimiento;
    @jakarta.persistence.Transient
    private transient List<Observer> observers = new CopyOnWriteArrayList<>();
    @jakarta.persistence.Lob
    private byte[] fotoPerfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Amigo> amigos = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrupoAmigos> grupos = new ArrayList<>();
    
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albumes = new ArrayList<>();

    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "usuario_muro_permisos",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private List<GrupoAmigos> gruposConPermisoEnMuro = new ArrayList<>();

    
    @Override
    public void attach(Observer o) {
        if (observers == null) observers = new CopyOnWriteArrayList<>();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void detach(Observer o) {
        if (observers != null) observers.remove(o);
    }

    @Override
    public void notifyObservers(String event) {
        if (observers != null) {
            for (Observer o : observers) {
                o.update(event, this);
            }
        }
    }

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

    public String getRol() { return this.rol; }
    public void setRol(String rol) { this.rol = rol; }


    public UsuarioEstado getEstado() { return this.estado; }

    public void setEstado(UsuarioEstado estado) { this.estado = estado; }

    public int getDiasNotificacionCumple() { return this.diasNotificacionCumple; }

    public void setDiasNotificacionCumple(int diasNotificacionCumple) { this.diasNotificacionCumple = diasNotificacionCumple; }

    public LocalDate getFechaNacimiento() { return this.fechaNacimiento; }

    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public byte[] getFotoPerfil() { return this.fotoPerfil; }

    public void setFotoPerfil(byte[] fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public List<Amigo> getAmigos() { return this.amigos; }

    public void setAmigos(List<Amigo> amigos) { this.amigos = amigos; }

    public List<GrupoAmigos> getGrupos() { return this.grupos; }

    public void setGrupos(List<GrupoAmigos> grupos) { this.grupos = grupos; }

    public List<Album> getAlbumes() { return this.albumes; }

    public void setAlbumes(List<Album> albumes) { this.albumes = albumes; }

    public List<GrupoAmigos> getGruposConPermisoEnMuro() { return this.gruposConPermisoEnMuro; }
    public void setGruposConPermisoEnMuro(List<GrupoAmigos> gruposConPermisoEnMuro) { this.gruposConPermisoEnMuro = gruposConPermisoEnMuro; }

    public List<Notificacion> getNotificaciones() { return this.notificaciones; }

    public void setNotificaciones(List<Notificacion> notificaciones) { this.notificaciones = notificaciones; }

}
