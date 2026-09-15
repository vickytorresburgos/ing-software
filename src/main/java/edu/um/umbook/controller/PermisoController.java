package edu.um.umbook.controller;
import edu.um.umbook.model.TipoPermiso;
import edu.um.umbook.service.PermisoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/permisos")
public class PermisoController {
    
    private final PermisoService permisoService;
    
    public PermisoController(PermisoService permisoService) {
        this.permisoService = permisoService;
    }

    @PostMapping
    public String updatePermisos(@RequestParam Long objetoId, @RequestParam String tipoObjeto, @RequestParam Long grupoId, @RequestParam(required = false) List<TipoPermiso> permisos, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(permisos == null) permisos = List.of();
        permisoService.asignarPermisos(objetoId, tipoObjeto, grupoId, permisos, userDetails.getUsuario());
        return "redirect:/groups?permissions_updated";
    }
}
