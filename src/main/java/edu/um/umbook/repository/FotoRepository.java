package edu.um.umbook.repository;
import edu.um.umbook.model.Foto;
import edu.um.umbook.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FotoRepository extends JpaRepository<Foto, Long> {
    List<Foto> findByAlbum(Album album);
    Optional<Foto> findByIdAndAlbum(Long id, Album album);
    List<Foto> findByIdInAndAlbum(Collection<Long> ids, Album album);
}
