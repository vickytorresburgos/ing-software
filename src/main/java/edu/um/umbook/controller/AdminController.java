package edu.um.umbook.controller;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String admin(Model model, @RequestParam(required = false) String query) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("usuarios", userService.searchUsers(query));
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
