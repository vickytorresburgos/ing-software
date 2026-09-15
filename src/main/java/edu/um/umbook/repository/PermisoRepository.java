package edu.um.umbook.repository;

import edu.um.umbook.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    List<Permiso> findByObjetoIdAndTipoObjeto(Long objetoId, String tipoObjeto);
    void deleteByObjetoIdAndTipoObjetoAndGrupoId(Long objetoId, String tipoObjeto, Long grupoId);
}
