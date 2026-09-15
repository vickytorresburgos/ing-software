package edu.um.umbook.controller;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.service.FriendService;
import edu.um.umbook.service.UserService;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
        model.addAttribute("amigos", friendService.getAmigosDe(me));
        return "friends"; // assuming views are in friends/ directory
    }
    
    @GetMapping("/search")
    public String searchUsers(@RequestParam(required = false) String q, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Usuario me = userDetails.getUsuario();
        if (q != null && !q.isEmpty()) {
            List<Usuario> resultados = userService.searchUsers(q).stream()
                .filter(u -> !u.getId().equals(me.getId()))
                .toList();
            model.addAttribute("usuarios", resultados);
            
            Map<Long, String> statusMap = new HashMap<>();
            for (Usuario u : resultados) {
                statusMap.put(u.getId(), friendService.getFriendshipStatus(me, u));
            }
            model.addAttribute("statusMap", statusMap);
        }
        return "friends-search";
    }

    @PostMapping("/add/{id}")
    public String addFriend(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Usuario me = userDetails.getUsuario();
        Usuario receptor = userService.findById(id); // I need to ensure this method exists
        if (receptor != null) {
            friendService.sendRequest(me, receptor);
        }
        return "redirect:/friends/search?q="; // redirect somewhere useful, or with flash message
    }
    
    @PostMapping("/remove/{id}")
    public String removeFriend(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Usuario me = userDetails.getUsuario();
        Usuario amigo = userService.findById(id);
        if (amigo != null) {
            friendService.removeFriend(me, amigo);
        }
        return "redirect:/friends";
    }

    @GetMapping("/requests")
    public String listRequests(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Usuario me = userDetails.getUsuario();
        model.addAttribute("solicitudes", friendService.getReceivedRequests(me));
        return "friends-requests";
    }

    @PostMapping("/requests/{id}/accept")
    public String acceptRequest(@PathVariable Long id) {
        friendService.acceptRequest(id);
        return "redirect:/friends/requests";
    }

    @PostMapping("/requests/{id}/reject")
    public String rejectRequest(@PathVariable Long id) {
        friendService.rejectRequest(id);
        return "redirect:/friends/requests";
    }
}
