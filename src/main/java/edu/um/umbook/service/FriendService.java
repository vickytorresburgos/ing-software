package edu.um.umbook.service;
import edu.um.umbook.model.*;
import edu.um.umbook.repository.*;
import edu.um.umbook.pattern.factory.SolicitudAmistadFactory;
import edu.um.umbook.pattern.state.SolicitudAmistadStateContext;
import edu.um.umbook.pattern.state.AcceptedState;
import edu.um.umbook.pattern.state.RejectedState;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class FriendService {
    private final AmigoRepository amigoRepository;
    private final SolicitudAmistadRepository solicitudRepository;
    private final NotificationService notificationService;
    
    public FriendService(AmigoRepository amigoRepository, SolicitudAmistadRepository solicitudRepository, NotificationService notificationService) {
        this.amigoRepository = amigoRepository;
        this.solicitudRepository = solicitudRepository;
        this.notificationService = notificationService;
    }
    
    public void sendRequest(Usuario solicitante, Usuario receptor) {
        SolicitudAmistad solicitud = SolicitudAmistadFactory.createSolicitud(solicitante, receptor);
        solicitudRepository.save(solicitud);
        notificationService.update(receptor, solicitante.getNombre() + " te ha enviado una solicitud de amistad.");
    }
    
    public void acceptRequest(Long solicitudId) {
        SolicitudAmistad solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        SolicitudAmistadStateContext context = new SolicitudAmistadStateContext(solicitud);
        context.setState(new AcceptedState());
        context.handle(solicitud);
        solicitudRepository.save(solicitud);
        
        Amigo a1 = new Amigo();
        a1.setUsuario(solicitud.getSolicitante());
        a1.setAmigoUsuario(solicitud.getReceptor());
        a1.setFechaAmistad(LocalDate.now());
        amigoRepository.save(a1);
        
        Amigo a2 = new Amigo();
        a2.setUsuario(solicitud.getReceptor());
        a2.setAmigoUsuario(solicitud.getSolicitante());
        a2.setFechaAmistad(LocalDate.now());
        amigoRepository.save(a2);
    }
    
    public void rejectRequest(Long solicitudId) {
        SolicitudAmistad solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        SolicitudAmistadStateContext context = new SolicitudAmistadStateContext(solicitud);
        context.setState(new RejectedState());
        context.handle(solicitud);
        solicitudRepository.save(solicitud);
    }
    
    public void removeFriend(Usuario u1, Usuario u2) {
        amigoRepository.findByUsuarioAndAmigoUsuario(u1, u2).ifPresent(amigoRepository::delete);
        amigoRepository.findByUsuarioAndAmigoUsuario(u2, u1).ifPresent(amigoRepository::delete);
    }
}
