package edu.um.umbook.controller;

import edu.um.umbook.model.Usuario;
import edu.um.umbook.repository.UsuarioRepository;
import edu.um.umbook.security.CustomUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import edu.um.umbook.service.FriendService;
import edu.um.umbook.service.NotificationService;
import edu.um.umbook.model.Amigo;
import java.util.List;

import java.io.IOException;

@Controller
@RequestMapping("/profile/picture")
public class ProfileController {

    private final UsuarioRepository usuarioRepository;
    private final FriendService friendService;
    private final NotificationService notificationService;

    public ProfileController(UsuarioRepository usuarioRepository, FriendService friendService, NotificationService notificationService) {
        this.usuarioRepository = usuarioRepository;
        this.friendService = friendService;
        this.notificationService = notificationService;
    }

    @PostMapping
    public String uploadPicture(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Por favor selecciona un archivo para subir.");
            return "redirect:/";
        }
        
        try {
            Usuario usuario = usuarioRepository.findById(userDetails.getUsuario().getId()).orElseThrow();
            usuario.setFotoPerfil(file.getBytes());
            
            usuarioRepository.save(usuario);
            
            // Notify friends that the profile was updated (CU0005)
            usuario.attach(notificationService);
            usuario.notifyObservers("perfil_actualizado");
            
            redirectAttributes.addFlashAttribute("mensaje", "Foto de perfil actualizada exitosamente.");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la imagen.");
        }
        
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deletePicture(@AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findById(userDetails.getUsuario().getId()).orElseThrow();
        usuario.setFotoPerfil(null);
        
        usuarioRepository.save(usuario);
        
        // Notify friends that the profile was updated (CU0005)
        usuario.attach(notificationService);
        usuario.notifyObservers("perfil_actualizado");
        
        redirectAttributes.addFlashAttribute("mensaje", "Foto de perfil eliminada.");

        return "redirect:/";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null || usuario.getFotoPerfil() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(usuario.getFotoPerfil(), headers, HttpStatus.OK);
    }
}
