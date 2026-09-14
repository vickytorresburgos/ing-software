package edu.um.umbook.service;
import edu.um.umbook.model.*;
import edu.um.umbook.repository.*;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private final GrupoAmigosRepository grupoRepository;
    
    public GroupService(GrupoAmigosRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }
    
    public void createGroup(GrupoAmigos grupo) {
        grupoRepository.save(grupo);
    }
    
    public void updatePermissions(Long groupId, boolean verAlbum, boolean comentarAlbum, boolean comentarMuro) {
        GrupoAmigos grupo = grupoRepository.findById(groupId).orElseThrow();
        grupo.setPuedeVerAlbum(verAlbum);
        grupo.setPuedeComentarAlbum(comentarAlbum);
        grupo.setPuedeComentarMuro(comentarMuro);
        grupoRepository.save(grupo);
    }
}
