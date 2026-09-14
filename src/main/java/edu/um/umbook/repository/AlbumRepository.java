package edu.um.umbook.repository;
import edu.um.umbook.model.Album;
import edu.um.umbook.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByCreador(Usuario creador);
}
