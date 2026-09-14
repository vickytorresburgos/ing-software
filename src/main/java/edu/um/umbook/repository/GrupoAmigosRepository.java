package edu.um.umbook.repository;
import edu.um.umbook.model.GrupoAmigos;
import edu.um.umbook.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GrupoAmigosRepository extends JpaRepository<GrupoAmigos, Long> {
    List<GrupoAmigos> findByUsuario(Usuario usuario);
}
