package edu.um.umbook.config;

import edu.um.umbook.model.*;
import edu.um.umbook.pattern.factory.SolicitudAmistadFactory;
import edu.um.umbook.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import org.springframework.http.MediaType;

@Configuration
public class DataInitializer {
    
    @Bean
    @Transactional
    public CommandLineRunner initData(
        UsuarioRepository usuarioRepository, 
        PasswordEncoder passwordEncoder,
        AmigoRepository amigoRepository,
        SolicitudAmistadRepository solicitudRepository,
        GrupoAmigosRepository grupoRepository,
        AlbumRepository albumRepository,
        FotoRepository fotoRepository,
        ComentarioRepository comentarioRepository,
        NotificacionRepository notificacionRepository) {
        
        return args -> {
            // Admins
            if(usuarioRepository.findByUsername("admin1").isEmpty()) {
                Usuario admin1 = new Usuario();
                admin1.setNombre("Super");
                admin1.setApellido("Admin");
                admin1.setUsername("admin1");
            admin1.setRol("ROLE_ADMIN");
                admin1.setPassword(passwordEncoder.encode("admin"));
                admin1.setEmail("admin1@umbook.edu.um");
                admin1.setEstado(UsuarioEstado.ACTIVO);
                usuarioRepository.save(admin1);
            }
            if(usuarioRepository.findByUsername("admin2").isEmpty()) {
                Usuario admin2 = new Usuario();
                admin2.setNombre("Second");
                admin2.setApellido("Admin");
                admin2.setUsername("admin2");
            admin2.setRol("ROLE_ADMIN");
                admin2.setPassword(passwordEncoder.encode("admin"));
                admin2.setEmail("admin2@umbook.edu.um");
                admin2.setEstado(UsuarioEstado.ACTIVO);
                usuarioRepository.save(admin2);
            }

            // Demo Users
            List<String> usernames = Arrays.asList("tdrube", "vtorres", "vcara", "jvalverde", "fquiroga");
            List<String> nombres = Arrays.asList("Tadeo", "Victoria", "Vicente", "Juan Martin", "Franco");
            List<String> apellidos = Arrays.asList("Drube", "Torres", "Cara", "Valverde", "Quiroga");
            
            Usuario[] demoUsers = new Usuario[5];
            
            for(int i = 0; i < 5; i++) {
                if(usuarioRepository.findByUsername(usernames.get(i)).isEmpty()) {
                    Usuario u = new Usuario();
                    u.setNombre(nombres.get(i));
                    u.setApellido(apellidos.get(i));
                    u.setUsername(usernames.get(i));
                    u.setPassword(passwordEncoder.encode("demo"));
                    u.setEmail(usernames.get(i) + "@umbook.edu.um");
                    u.setEstado(UsuarioEstado.ACTIVO);
                    
                    // Birthdays for testing
                    if (i == 1) { // Victoria has birthday today
                        u.setFechaNacimiento(LocalDate.now());
                    } else if (i == 2) { // Vicente tomorrow
                        u.setFechaNacimiento(LocalDate.now().plusDays(1));
                    } else {
                        u.setFechaNacimiento(LocalDate.of(1995, 5, 10));
                    }
                    
                    demoUsers[i] = usuarioRepository.save(u);
                } else {
                    demoUsers[i] = usuarioRepository.findByUsername(usernames.get(i)).get();
                }
            }
            
            Usuario tadeo = demoUsers[0];
            Usuario victoria = demoUsers[1];
            Usuario vicente = demoUsers[2];
            Usuario juan = demoUsers[3];
            Usuario franco = demoUsers[4];

            Path profileDir = Paths.get("profile");
            if (Files.exists(profileDir)) {
                for (int i = 0; i < 5; i++) {
                    Path imgPath = profileDir.resolve("pphoto" + (i+1) + ".jpeg");
                    if (Files.exists(imgPath)) {
                        demoUsers[i].setFotoPerfil(Files.readAllBytes(imgPath));
                        usuarioRepository.save(demoUsers[i]);
                    }
                }
            }


            // Friendships (if not exists)
            crearAmistad(amigoRepository, tadeo, victoria);
            crearAmistad(amigoRepository, tadeo, vicente);
            crearAmistad(amigoRepository, juan, franco);
            
            // Pending request from Franco to Tadeo
            if (!solicitudRepository.existsBySolicitanteAndReceptorAndEstado(franco, tadeo, "PENDING")) {
                SolicitudAmistad req = SolicitudAmistadFactory.createSolicitud(franco, tadeo);
                solicitudRepository.save(req);
                
                // NOTIFICATION: Solicitud de amistad (As specified in CU0005)
                crearNotificacion(notificacionRepository, tadeo, "Franco Quiroga te ha enviado una solicitud de amistad.");
            }

            // Groups for Tadeo
            if (grupoRepository.findByUsuario(tadeo).isEmpty()) {
                GrupoAmigos cercanos = new GrupoAmigos();
                cercanos.setNombre("Amigos Cercanos");
                cercanos.setDescripcion("Mi grupo íntimo");
                cercanos.setUsuario(tadeo);
                
                List<Amigo> list1 = amigoRepository.findByUsuarioAndAmigoUsuario(tadeo, victoria);
                Amigo amigoVic1 = list1.isEmpty() ? null : list1.get(0);
                
                List<Amigo> list2 = amigoRepository.findByUsuarioAndAmigoUsuario(tadeo, vicente);
                Amigo amigoVic2 = list2.isEmpty() ? null : list2.get(0);
                
                if (amigoVic1 != null) cercanos.getMiembros().add(amigoVic1);
                if (amigoVic2 != null) cercanos.getMiembros().add(amigoVic2);
                
                grupoRepository.save(cercanos);
                
                // NOTIFICATION: Has sido agregado al grupo (Diagrama-Secuencia-Crear-Grupo.md)
                crearNotificacion(notificacionRepository, victoria, "Tadeo Drube te ha agregado al grupo Amigos Cercanos.");
                crearNotificacion(notificacionRepository, vicente, "Tadeo Drube te ha agregado al grupo Amigos Cercanos.");
                
                GrupoAmigos familia = new GrupoAmigos();
                familia.setNombre("Familia");
                familia.setDescripcion("Grupo familiar");
                familia.setUsuario(tadeo);
                grupoRepository.save(familia);
                
                // Set permissions to wall using the user entity
                tadeo.getGruposConPermisoEnMuro().add(cercanos);
                usuarioRepository.save(tadeo);
                
                // NOTIFICATION: Permisos actualizados (Diagrama-Secuencia-Configurar-Permisos.md)
                crearNotificacion(notificacionRepository, victoria, "Permisos actualizados en el grupo Amigos Cercanos");
            }
            
            // Wall comments
            if (comentarioRepository.count() == 0) {
                // Comentario en muro propio (Tadeo publica en su muro)
                Comentario muro1 = new Comentario();
                muro1.setContenido("Hola a todos, este es mi primer post en mi muro!");
                muro1.setAutor(tadeo);
                muro1.setMuroDestino(tadeo);
                muro1.setEstado(ComentarioEstado.CREADO);
                muro1.setFechaCreacion(LocalDateTime.now().minusHours(2));
                comentarioRepository.save(muro1);
                
                // Comentario en muro de amigo (Victoria publica en muro de Tadeo)
                Comentario muro2 = new Comentario();
                muro2.setContenido("Hola Tadeo, nos vemos más tarde!");
                muro2.setAutor(victoria);
                muro2.setMuroDestino(tadeo);
                muro2.setEstado(ComentarioEstado.CREADO);
                muro2.setFechaCreacion(LocalDateTime.now().minusHours(1));
                comentarioRepository.save(muro2);
            }
            
            // Other notifications based on docs
            if (notificacionRepository.count() <= 4) { // Only if we haven't added these yet
                // Amigo modificó su perfil
                crearNotificacion(notificacionRepository, tadeo, "Vicente Cara ha actualizado su perfil.");
                crearNotificacion(notificacionRepository, tadeo, "¡Recuerda saludar a Victoria Torres por su cumpleaños!");
            }
            
            // Albums and photos for Tadeo
            if (albumRepository.findByCreador(tadeo).isEmpty()) {
                Album viaje = new Album("Viajes 2026", "Fotos de mis vacaciones", tadeo);
                albumRepository.save(viaje);
                
                Path sourceDir = Paths.get("images");
                Path uploadDir = Paths.get("uploads");
                
                if (Files.exists(sourceDir)) {
                    if (!Files.exists(uploadDir)) {
                        Files.createDirectories(uploadDir);
                    }
                    
                    File[] files = sourceDir.toFile().listFiles((d, name) -> name.endsWith(".jpg"));
                    if (files != null) {
                        int count = 0;
                        String[] comentariosVariados = {
                            "¡Qué lugar tan increíble!",
                            "Hermosa vista, ojalá estuviera ahí.",
                            "Espectacular, ¿dónde es eso?",
                            "Muy buena foto, los colores son geniales.",
                            "Me encanta, tengo que ir a visitarlo!"
                        };
                        Usuario[] autoresComentarios = {victoria, vicente, juan, franco, victoria};
                        
                        for (File f : files) {
                            if (count >= 5) break; 
                            
                            String uniqueName = UUID.randomUUID().toString() + ".jpg";
                            Path dest = uploadDir.resolve(uniqueName);
                            Files.copy(f.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                            
                            Foto foto = new Foto(dest.toString(), f.getName(), MediaType.IMAGE_JPEG_VALUE, viaje);
                            foto = fotoRepository.save(foto);
                            
                            // Create varied comments on photos
                            Comentario c = new Comentario(comentariosVariados[count], foto, autoresComentarios[count]);
                            comentarioRepository.save(c);
                            
                            // NOTIFICATION: Nueva foto en el álbum
                            if (count == 0) {
                                crearNotificacion(notificacionRepository, victoria, "Tadeo Drube subió una nueva foto al álbum Viajes 2026.");
                            }
                            
                            // NOTIFICATION: Amigo comentó en foto propia
                            crearNotificacion(notificacionRepository, tadeo, autoresComentarios[count].getNombre() + " " + autoresComentarios[count].getApellido() + " comentó en tu foto.");
                            
                            count++;
                        }
                    }
                }
            }

            System.out.println("Data seeder completed.");
        };
    }
    
    private void crearAmistad(AmigoRepository amigoRepo, Usuario u1, Usuario u2) {
        if (amigoRepo.findByUsuarioAndAmigoUsuario(u1, u2).isEmpty()) {
            Amigo a1 = new Amigo();
            a1.setUsuario(u1);
            a1.setAmigoUsuario(u2);
            a1.setFechaAmistad(LocalDate.now());
            amigoRepo.save(a1);
            
            Amigo a2 = new Amigo();
            a2.setUsuario(u2);
            a2.setAmigoUsuario(u1);
            a2.setFechaAmistad(LocalDate.now());
            amigoRepo.save(a2);
        }
    }
    
    private void crearNotificacion(NotificacionRepository notRepo, Usuario dest, String mensaje) {
        Notificacion n = new Notificacion();
        n.setDestinatario(dest);
        n.setMensaje(mensaje);
        n.setFechaCreacion(LocalDateTime.now());
        n.setLeida(false);
        notRepo.save(n);
    }
}
