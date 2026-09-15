package edu.um.umbook.controller;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(usuario);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
