package edu.um.umbook.controller;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.service.FriendService;
import edu.um.umbook.service.UserService;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/friends")
public class FriendController {
    
    private final FriendService friendService;
    private final UserService userService;
    
    public FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }
    
    @GetMapping
    public String listFriends(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Usuario me = userDetails.getUsuario();
        model.addAttribute("amigos", me.getAmigos());
        return "friends";
    }
    
    @PostMapping("/add/{id}")
    public String addFriend(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Implementation for CU0009
        return "redirect:/friends";
    }
    
    @PostMapping("/remove/{id}")
    public String removeFriend(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Implementation for CU0010
        return "redirect:/friends";
    }
}
