package edu.um.umbook.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if(userDetails != null) {
            model.addAttribute("usuario", userDetails.getUsuario());
        }
        return "home";
    }
}
