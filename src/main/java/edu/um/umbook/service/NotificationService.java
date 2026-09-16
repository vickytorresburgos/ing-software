package edu.um.umbook.service;
import edu.um.umbook.model.Notificacion;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.pattern.observer.Observer;
import edu.um.umbook.repository.AmigoRepository;
import edu.um.umbook.model.Amigo;
import edu.um.umbook.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationService implements Observer {
    private final NotificacionRepository notificacionRepository;
    private final AmigoRepository amigoRepository;
    
    public NotificationService(NotificacionRepository notificacionRepository, AmigoRepository amigoRepository) {
        this.notificacionRepository = notificacionRepository;
        this.amigoRepository = amigoRepository;
    }
    
    
    
    @Override
    
    public void update(String event, Object data) {
        if (data instanceof Usuario usuario) {
            if ("perfil_actualizado".equals(event)) {
                for (Amigo amigo : amigoRepository.findByUsuario(usuario)) {
                    notifyUser(amigo.getAmigoUsuario(), usuario.getNombre() + " ha actualizado su perfil.");
                }
            }
        }
    }

    public void notifyUser(Usuario destinatario, String mensaje) {
        Notificacion notif = new Notificacion();
        notif.setDestinatario(destinatario);
        notif.setMensaje(mensaje);
        notif.setFechaCreacion(LocalDateTime.now());
        notificacionRepository.save(notif);
    }
}
