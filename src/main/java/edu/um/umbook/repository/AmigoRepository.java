package edu.um.umbook.repository;
import edu.um.umbook.model.Amigo;
import edu.um.umbook.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AmigoRepository extends JpaRepository<Amigo, Long> {
    List<Amigo> findByUsuario(Usuario usuario);
    List<Amigo> findByUsuarioAndAmigoUsuario(Usuario usuario, Usuario amigo);
    boolean existsByUsuarioAndAmigoUsuario(Usuario usuario, Usuario amigo);
}
