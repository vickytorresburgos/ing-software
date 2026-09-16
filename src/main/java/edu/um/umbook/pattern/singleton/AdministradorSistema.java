package edu.um.umbook.pattern.singleton;

import edu.um.umbook.model.Usuario;
import edu.um.umbook.model.Comentario;
import edu.um.umbook.model.ComentarioEstado;

public class AdministradorSistema {
    private static AdministradorSistema instance;
    
    private AdministradorSistema() {}
    
    public static synchronized AdministradorSistema getInstance() {
        if (instance == null) {
            instance = new AdministradorSistema();
        }
        return instance;
    }
    
    public void deshabilitarUsuario(Usuario user) {
        if (user.getEstado() == edu.um.umbook.model.UsuarioEstado.ACTIVO) {
            user.setEstado(edu.um.umbook.model.UsuarioEstado.DESHABILITADO);
        } else if (user.getEstado() == edu.um.umbook.model.UsuarioEstado.DESHABILITADO) {
            user.setEstado(edu.um.umbook.model.UsuarioEstado.ACTIVO);
        }
    }

    public void eliminarComentario(Comentario comentario) {
        comentario.modificarContenido("El comentario ha sido eliminado por el administrador");
        comentario.setEstado(ComentarioEstado.ELIMINADO);
    }
}
