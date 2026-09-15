package edu.um.umbook.repository;
import edu.um.umbook.model.SolicitudAmistad;
import edu.um.umbook.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SolicitudAmistadRepository extends JpaRepository<SolicitudAmistad, Long> {
    List<SolicitudAmistad> findByReceptorAndEstado(Usuario receptor, String estado);
    boolean existsBySolicitanteAndReceptorAndEstado(Usuario solicitante, Usuario receptor, String estado);
}
