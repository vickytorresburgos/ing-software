package edu.um.umbook.service;

import edu.um.umbook.model.Usuario;
import edu.um.umbook.model.Comentario;
import edu.um.umbook.model.ComentarioEstado;
import edu.um.umbook.repository.UsuarioRepository;
import edu.um.umbook.repository.ComentarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.um.umbook.pattern.singleton.AdministradorSistema;

import java.util.List;

@Service
public class UserService {
    private final UsuarioRepository usuarioRepository;
    private final ComentarioRepository comentarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    public UserService(UsuarioRepository usuarioRepository, ComentarioRepository comentarioRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.comentarioRepository = comentarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
    
    public void registerUser(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail()) || usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El email o username ya existe.");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(edu.um.umbook.model.UsuarioEstado.ACTIVO);
        usuarioRepository.save(usuario);
        emailService.enviarEmailBienvenida(usuario.getEmail());
    }
    
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }
    
    public List<Usuario> searchUsers(String query) {
        return usuarioRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(query, query);
    }
    
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    public void updateDiasNotificacion(Usuario user, int dias) {
        user.setDiasNotificacionCumple(dias);
        usuarioRepository.save(user);
    }
    
    public void toggleUserStatus(Long userId) {
        Usuario user = usuarioRepository.findById(userId).orElseThrow();
        AdministradorSistema.getInstance().deshabilitarUsuario(user);
        usuarioRepository.save(user);
    }
    
    public void deleteCommentAsAdmin(Long commentId) {
        Comentario comentario = comentarioRepository.findById(commentId).orElseThrow();
        AdministradorSistema.getInstance().eliminarComentario(comentario);
        comentarioRepository.save(comentario);
    }
}
