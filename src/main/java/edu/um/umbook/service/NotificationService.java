package edu.um.umbook.service;
import edu.um.umbook.model.Notificacion;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.pattern.observer.NotificationObserver;
import edu.um.umbook.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationService implements NotificationObserver {
    private final NotificacionRepository notificacionRepository;
    
    public NotificationService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }
    
    @Override
    public void update(Usuario destinatario, String mensaje) {
        Notificacion notif = new Notificacion();
        notif.setDestinatario(destinatario);
        notif.setMensaje(mensaje);
        notif.setFechaCreacion(LocalDateTime.now());
        notificacionRepository.save(notif);
    }
}
