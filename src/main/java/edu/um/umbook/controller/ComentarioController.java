package edu.um.umbook.controller;

import edu.um.umbook.model.Foto;
import edu.um.umbook.security.CustomUserDetails;
import edu.um.umbook.service.AlbumService;
import edu.um.umbook.service.ComentarioService;
import edu.um.umbook.service.FotoService;
import edu.um.umbook.web.form.ComentarioForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/albums/{idAlbum}/photos/{idFoto}/comments")
public class ComentarioController {

  private final AlbumService albumService;
  private final FotoService fotoService;
  private final ComentarioService comentarioService;

  public ComentarioController(
    AlbumService a,
    FotoService f,
    ComentarioService c
  ) {
    albumService = a;
    fotoService = f;
    comentarioService = c;
  }

  @PostMapping
  public String agregar(
    @PathVariable Long idAlbum,
    @PathVariable Long idFoto,
    @AuthenticationPrincipal CustomUserDetails u,
    @Valid ComentarioForm f,
    BindingResult r,
    RedirectAttributes a
  ) {
    return guardar(idAlbum, idFoto, null, u, f, r, a);
  }

  @PostMapping("/{idComentario}/edit")
  public String modificar(
    @PathVariable Long idAlbum,
    @PathVariable Long idFoto,
    @PathVariable Long idComentario,
    @AuthenticationPrincipal CustomUserDetails u,
    @Valid ComentarioForm f,
    BindingResult r,
    RedirectAttributes a
  ) {
    return guardar(idAlbum, idFoto, idComentario, u, f, r, a);
  }

  @PostMapping("/{idComentario}/delete")
  public String eliminar(
    @PathVariable Long idAlbum,
    @PathVariable Long idFoto,
    @PathVariable Long idComentario,
    @AuthenticationPrincipal CustomUserDetails u,
    RedirectAttributes a
  ) {
    try {
      comentarioService.eliminarComentarioDeFotoPropia(
        idComentario,
        foto(idAlbum, idFoto, u)
      );
      a.addFlashAttribute("mensaje", "Comentario eliminado correctamente.");
    } catch (RuntimeException e) {
      a.addFlashAttribute("error", e.getMessage());
    }
    return redireccion(idAlbum, idFoto);
  }

  private String guardar(
    Long aId,
    Long fId,
    Long cId,
    CustomUserDetails u,
    ComentarioForm f,
    BindingResult r,
    RedirectAttributes a
  ) {
    if (r.hasErrors()) {
      a.addFlashAttribute(
        "error",
        "El comentario debe tener entre 1 y 250 caracteres."
      );
      return redireccion(aId, fId);
    }
    try {
      if (cId == null) comentarioService.agregarComentario(
        f.getContenido(),
        foto(aId, fId, u),
        u.getUsuario()
      );
      else comentarioService.modificarComentarioPropio(
        cId,
        foto(aId, fId, u),
        u.getUsuario(),
        f.getContenido()
      );
      a.addFlashAttribute(
        "mensaje",
        cId == null
          ? "Comentario agregado correctamente."
          : "Comentario modificado correctamente."
      );
    } catch (RuntimeException e) {
      a.addFlashAttribute("error", e.getMessage());
    }
    return redireccion(aId, fId);
  }

  private Foto foto(Long aId, Long fId, CustomUserDetails u) {
    return fotoService.obtenerFotoDelAlbum(
      fId,
      albumService.obtenerAlbumPropio(aId, u.getUsuario())
    );
  }

  private String redireccion(Long aId, Long fId) {
    return "redirect:/albums/" + aId + "/photos/" + fId;
  }
}
