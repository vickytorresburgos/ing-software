package edu.um.umbook.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.ui.Model;
import edu.um.umbook.service.FriendService;
import edu.um.umbook.repository.NotificacionRepository;
import edu.um.umbook.repository.ComentarioRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.um.umbook.model.Comentario;
import edu.um.umbook.model.Usuario;
import java.time.LocalDateTime;

@Controller
public class HomeController {
    private final FriendService friendService;
    private final NotificacionRepository notificacionRepository;
    private final ComentarioRepository comentarioRepository;
    public HomeController(FriendService friendService, NotificacionRepository notificacionRepository, ComentarioRepository comentarioRepository) {
        this.friendService = friendService;
        this.notificacionRepository = notificacionRepository;
        this.comentarioRepository = comentarioRepository;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if(userDetails != null) {
            if ("ROLE_ADMIN".equals(userDetails.getUsuario().getRol())) {
                return "redirect:/admin";
            }
            model.addAttribute("usuario", userDetails.getUsuario());
            model.addAttribute("cumpleaneros", friendService.getCumpleanosProximos(userDetails.getUsuario()));
            model.addAttribute("notificaciones", notificacionRepository.findByDestinatarioOrderByFechaCreacionDesc(userDetails.getUsuario()));
            model.addAttribute("comentariosMuro", comentarioRepository.findByMuroDestinoOrderByFechaCreacionDesc(userDetails.getUsuario()));
        }
        return "home";
    }

    @PostMapping("/wall/post")
    public String postToWall(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String contenido) {
        if(userDetails != null && contenido != null && !contenido.trim().isEmpty()) {
            Usuario me = userDetails.getUsuario();
            Comentario com = new Comentario();
            com.setAutor(me);
            com.setMuroDestino(me);
            com.setContenido(contenido);
            com.setFechaCreacion(LocalDateTime.now());
            com.setEstado(edu.um.umbook.model.ComentarioEstado.CREADO);
            comentarioRepository.save(com);
        }
        return "redirect:/";
    }

}
