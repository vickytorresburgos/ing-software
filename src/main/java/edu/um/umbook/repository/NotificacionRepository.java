package edu.um.umbook.repository;
import edu.um.umbook.model.Notificacion;
import edu.um.umbook.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByDestinatarioOrderByFechaCreacionDesc(Usuario destinatario);
}
