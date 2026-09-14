package edu.um.umbook.repository;
import edu.um.umbook.model.Comentario;
import edu.um.umbook.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByMuroDestinoOrderByFechaCreacionDesc(Usuario muroDestino);
}
