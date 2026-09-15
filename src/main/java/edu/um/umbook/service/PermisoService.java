package edu.um.umbook.service;
import edu.um.umbook.model.*;
import edu.um.umbook.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermisoService {
    private final PermisoRepository permisoRepository;
    private final GrupoAmigosRepository grupoRepository;
    private final NotificationService notificationService;
    
    public PermisoService(PermisoRepository permisoRepository, GrupoAmigosRepository grupoRepository, NotificationService notificationService) {
        this.permisoRepository = permisoRepository;
        this.grupoRepository = grupoRepository;
        this.notificationService = notificationService;
    }
    
    public void asignarPermisos(Long objetoId, String tipoObjeto, Long grupoId, List<TipoPermiso> tiposPermisos, Usuario user) {
        GrupoAmigos grupo = grupoRepository.findById(grupoId).orElseThrow();
        if (!grupo.getUsuario().getId().equals(user.getId())) {
            throw new IllegalArgumentException("No eres el propietario del grupo.");
        }
        
        permisoRepository.deleteByObjetoIdAndTipoObjetoAndGrupoId(objetoId, tipoObjeto, grupoId);
        
        for (TipoPermiso tipo : tiposPermisos) {
            Permiso p = new Permiso();
            p.setObjetoId(objetoId);
            p.setTipoObjeto(tipoObjeto);
            p.setGrupo(grupo);
            p.setPermiso(tipo);
            permisoRepository.save(p);
        }
        
        for (Amigo miembro : grupo.getMiembros()) {
            notificationService.update(miembro.getAmigoUsuario(), "Permisos actualizados para el grupo " + grupo.getNombre());
        }
    }
}
