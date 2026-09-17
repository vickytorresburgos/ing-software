package edu.um.umbook.controller;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.model.GrupoAmigos;
import edu.um.umbook.repository.UsuarioRepository;
import edu.um.umbook.service.GroupService;
import edu.um.umbook.service.FriendService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    
    private final GroupService groupService;
    private final FriendService friendService;
    private final UsuarioRepository usuarioRepository;
    
    public GroupController(GroupService groupService, FriendService friendService, UsuarioRepository usuarioRepository) {
        this.groupService = groupService;
        this.friendService = friendService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String groups(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Usuario user = userDetails.getUsuario();
        model.addAttribute("grupos", groupService.getGruposDeUsuario(user));
        model.addAttribute("misAmigos", friendService.getAmigosDe(user));
        return "groups";
    }
    
    @PostMapping
    public String createGroup(@RequestParam String nombre, @RequestParam String descripcion, @RequestParam(required = false) List<Long> amigosIds, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        if (nombre == null || nombre.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El nombre de grupo de amigos ingresado es inválido y debe probar con uno distinto.");
            return "redirect:/groups";
        }
        if(amigosIds == null) amigosIds = List.of();
        groupService.crearGrupo(nombre, descripcion, amigosIds, userDetails.getUsuario());
        redirectAttributes.addFlashAttribute("success", "Se ha creado el grupo correctamente.");
        return "redirect:/groups";
    }

    @PostMapping("/{id}/wall-permission")
    public String toggleWallPermission(@PathVariable Long id, @RequestParam boolean allow, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        Usuario me = userDetails.getUsuario();
        GrupoAmigos grupo = groupService.getGruposDeUsuario(me).stream().filter(g -> g.getId().equals(id)).findFirst().orElse(null);
        if(grupo != null) {
            Usuario dbMe = usuarioRepository.findById(me.getId()).get();
            if (allow && !dbMe.getGruposConPermisoEnMuro().contains(grupo)) {
                dbMe.getGruposConPermisoEnMuro().add(grupo);
            } else if (!allow) {
                dbMe.getGruposConPermisoEnMuro().remove(grupo);
            }
            usuarioRepository.save(dbMe);
            redirectAttributes.addFlashAttribute("success", "Permisos de muro actualizados para el grupo " + grupo.getNombre());
        }
        return "redirect:/groups";
    }
}
