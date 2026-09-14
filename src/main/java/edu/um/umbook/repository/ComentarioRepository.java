package edu.um.umbook.repository;
import edu.um.umbook.model.Comentario;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByMuroDestinoOrderByFechaCreacionDesc(Usuario muroDestino);
    List<Comentario> findByFotoOrderByFechaCreacionAsc(Foto foto);
    Optional<Comentario> findByIdAndFotoAndAutor(Long id, Foto foto, Usuario autor);
    Optional<Comentario> findByIdAndFoto(Long id, Foto foto);
    void deleteByFotoIn(Collection<Foto> fotos);
}
