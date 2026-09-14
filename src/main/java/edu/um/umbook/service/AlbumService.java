package edu.um.umbook.service;
import edu.um.umbook.model.*;
import edu.um.umbook.repository.*;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final FotoRepository fotoRepository;
    private final ComentarioRepository comentarioRepository;
    
    public AlbumService(AlbumRepository albumRepository, FotoRepository fotoRepository, ComentarioRepository comentarioRepository) {
        this.albumRepository = albumRepository;
        this.fotoRepository = fotoRepository;
        this.comentarioRepository = comentarioRepository;
    }
    
    public void createAlbum(Album album) {
        albumRepository.save(album);
    }
    
    public void addFoto(Foto foto) {
        fotoRepository.save(foto);
    }
    
    public void addComentario(Comentario comentario) {
        comentarioRepository.save(comentario);
    }
}
