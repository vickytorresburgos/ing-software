package edu.um.umbook.service;
import edu.um.umbook.model.*;
import edu.um.umbook.repository.*;
import edu.um.umbook.pattern.factory.SolicitudAmistadFactory;
import edu.um.umbook.pattern.state.SolicitudAmistadStateContext;
import edu.um.umbook.pattern.state.AcceptedState;
import edu.um.umbook.pattern.state.RejectedState;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.MonthDay;
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
        if (amigoRepository.existsByUsuarioAndAmigoUsuario(solicitante, receptor)) {
            return; // Ya son amigos
        }
        if (solicitudRepository.existsBySolicitanteAndReceptorAndEstado(solicitante, receptor, "PENDING")) {
            return; // Ya hay solicitud pendiente
        }
        if (solicitudRepository.existsBySolicitanteAndReceptorAndEstado(receptor, solicitante, "PENDING")) {
            return; // El otro usuario ya le envió solicitud
        }

        SolicitudAmistad solicitud = SolicitudAmistadFactory.createSolicitud(solicitante, receptor);
        solicitudRepository.save(solicitud);
        notificationService.notifyUser(receptor, solicitante.getNombre() + " te ha enviado una solicitud de amistad.");
    }
    
    public void acceptRequest(Long solicitudId) {
        SolicitudAmistad solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        if ("ACCEPTED".equals(solicitud.getEstado())) {
            return; // Ya fue aceptada
        }
        SolicitudAmistadStateContext context = new SolicitudAmistadStateContext(solicitud);
        context.setState(new AcceptedState());
        context.handle(solicitud);
        solicitudRepository.save(solicitud);
        
        if (!amigoRepository.existsByUsuarioAndAmigoUsuario(solicitud.getSolicitante(), solicitud.getReceptor())) {
            Amigo a1 = new Amigo();
            a1.setUsuario(solicitud.getSolicitante());
            a1.setAmigoUsuario(solicitud.getReceptor());
            a1.setFechaAmistad(LocalDate.now());
            amigoRepository.save(a1);
        }
        
        if (!amigoRepository.existsByUsuarioAndAmigoUsuario(solicitud.getReceptor(), solicitud.getSolicitante())) {
            Amigo a2 = new Amigo();
            a2.setUsuario(solicitud.getReceptor());
            a2.setAmigoUsuario(solicitud.getSolicitante());
            a2.setFechaAmistad(LocalDate.now());
            amigoRepository.save(a2);
        }
    }
    
    public void rejectRequest(Long solicitudId) {
        SolicitudAmistad solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        if (!"PENDING".equals(solicitud.getEstado())) {
            return;
        }
        SolicitudAmistadStateContext context = new SolicitudAmistadStateContext(solicitud);
        context.setState(new RejectedState());
        context.handle(solicitud);
        solicitudRepository.save(solicitud);
    }
    
    public void removeFriend(Usuario u1, Usuario u2) {
        amigoRepository.findByUsuarioAndAmigoUsuario(u1, u2).forEach(amigoRepository::delete);
        amigoRepository.findByUsuarioAndAmigoUsuario(u2, u1).forEach(amigoRepository::delete);
    }
    
    public List<Usuario> getCumpleanosProximos(Usuario usuario) {
        List<Amigo> amigos = amigoRepository.findByUsuario(usuario);
        LocalDate hoy = LocalDate.now();
        return amigos.stream()
            .map(Amigo::getAmigoUsuario)
            .filter(a -> a.getFechaNacimiento() != null)
            .filter(a -> {
                LocalDate bday = a.getFechaNacimiento();
                LocalDate bdayThisYear = bday.withYear(hoy.getYear());
                if (bdayThisYear.isBefore(hoy)) {
                    bdayThisYear = bdayThisYear.plusYears(1);
                }
                return !bdayThisYear.isAfter(hoy.plusDays(usuario.getDiasNotificacionCumple()));
            })
            .toList();
    }

    public List<Amigo> getAmigosDe(Usuario user) {
        return amigoRepository.findByUsuario(user);
    }

    public List<SolicitudAmistad> getReceivedRequests(Usuario user) {
        return solicitudRepository.findByReceptorAndEstado(user, "PENDING");
    }

    public String getFriendshipStatus(Usuario me, Usuario other) {
        if (amigoRepository.existsByUsuarioAndAmigoUsuario(me, other)) {
            return "AMIGO";
        }
        if (solicitudRepository.existsBySolicitanteAndReceptorAndEstado(me, other, "PENDING")) {
            return "PENDIENTE_ENVIADA";
        }
        if (solicitudRepository.existsBySolicitanteAndReceptorAndEstado(other, me, "PENDING")) {
            return "PENDIENTE_RECIBIDA";
        }
        return "NADA";
    }
}
