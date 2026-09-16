package edu.um.umbook.service;
import edu.um.umbook.model.*;
import edu.um.umbook.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupService {
    private final GrupoAmigosRepository grupoRepository;
    private final AmigoRepository amigoRepository;
    private final NotificationService notificationService;
    
    public GroupService(GrupoAmigosRepository grupoRepository, AmigoRepository amigoRepository, NotificationService notificationService) {
        this.grupoRepository = grupoRepository;
        this.amigoRepository = amigoRepository;
        this.notificationService = notificationService;
    }
    
    public void crearGrupo(String nombre, String descripcion, List<Long> amigosIds, Usuario owner) {
        GrupoAmigos grupo = new GrupoAmigos();
        grupo.setNombre(nombre);
        grupo.setDescripcion(descripcion);
        grupo.setUsuario(owner);
        
        List<Amigo> miembros = amigoRepository.findAllById(amigosIds);
        grupo.setMiembros(miembros);
        grupoRepository.save(grupo);
        
        for (Amigo miembro : miembros) {
            notificationService.notifyUser(miembro.getAmigoUsuario(), "Has sido agregado al grupo " + nombre);
        }
    }
    
    public List<GrupoAmigos> getGruposDeUsuario(Usuario user) {
        return grupoRepository.findByUsuario(user);
    }
}
