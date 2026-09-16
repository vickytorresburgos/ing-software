package edu.um.umbook.controller;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.service.UserService;
import edu.um.umbook.repository.ComentarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ComentarioRepository comentarioRepository;
    
    public AdminController(UserService userService, ComentarioRepository comentarioRepository) {
        this.userService = userService;
        this.comentarioRepository = comentarioRepository;
    }

    @GetMapping
    public String admin(Model model, @RequestParam(required = false) String query, @RequestParam(required = false) String queryComentario, @RequestParam(required = false, defaultValue = "usuarios") String tab) {
        model.addAttribute("tab", tab);
        if (query != null && !query.isEmpty()) {
            model.addAttribute("usuarios", userService.searchUsers(query));
        }
        if (queryComentario != null && !queryComentario.isEmpty()) {
            model.addAttribute("comentariosBuscados", comentarioRepository.findByContenidoContainingIgnoreCase(queryComentario));
        }
        return "admin";
    }
    
    @PostMapping("/usuarios/{id}/toggle")
    public String toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin?updated";
    }
    
    @PostMapping("/comentarios/eliminar")
    public String deleteCommentAdmin(@RequestParam Long commentId) {
        userService.deleteCommentAsAdmin(commentId);
        return "redirect:/admin?comment_deleted";
    }
}
