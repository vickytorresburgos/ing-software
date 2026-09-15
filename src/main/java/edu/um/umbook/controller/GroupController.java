package edu.um.umbook.controller;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.service.GroupService;
import edu.um.umbook.service.FriendService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    
    private final GroupService groupService;
    private final FriendService friendService;
    
    public GroupController(GroupService groupService, FriendService friendService) {
        this.groupService = groupService;
        this.friendService = friendService;
    }

    @GetMapping
    public String groups(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Usuario user = userDetails.getUsuario();
        model.addAttribute("grupos", groupService.getGruposDeUsuario(user));
        model.addAttribute("misAmigos", friendService.getAmigosDe(user));
        return "groups";
    }
    
    @PostMapping
    public String createGroup(@RequestParam String nombre, @RequestParam String descripcion, @RequestParam(required = false) List<Long> amigosIds, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(amigosIds == null) amigosIds = List.of();
        groupService.crearGrupo(nombre, descripcion, amigosIds, userDetails.getUsuario());
        return "redirect:/groups?created";
    }
}
