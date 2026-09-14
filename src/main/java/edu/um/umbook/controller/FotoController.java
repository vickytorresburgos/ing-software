package edu.um.umbook.controller;

import edu.um.umbook.model.Album;
import edu.um.umbook.model.Foto;
import edu.um.umbook.security.CustomUserDetails;
import edu.um.umbook.service.AlbumService;
import edu.um.umbook.service.ComentarioService;
import edu.um.umbook.service.FotoService;
import edu.um.umbook.web.form.ComentarioForm;
import edu.um.umbook.web.form.FotosForm;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/albums/{idAlbum}/photos")
public class FotoController {

  private final AlbumService albumService;
  private final FotoService fotoService;
  private final ComentarioService comentarioService;

  public FotoController(AlbumService a, FotoService f, ComentarioService c) {
    albumService = a;
    fotoService = f;
    comentarioService = c;
  }

  @GetMapping("/new")
  public String nuevo(
    @PathVariable Long idAlbum,
    @AuthenticationPrincipal CustomUserDetails u,
    Model m
  ) {
    m.addAttribute(
      "album",
      albumService.obtenerAlbumPropio(idAlbum, u.getUsuario())
    );
    m.addAttribute("fotosForm", new FotosForm());
    return "photo-form";
  }

  @PostMapping
  public String subir(
    @PathVariable Long idAlbum,
    @AuthenticationPrincipal CustomUserDetails u,
    @Valid @ModelAttribute("fotosForm") FotosForm f,
    BindingResult r,
    Model m,
    RedirectAttributes a
  ) {
    Album album = albumService.obtenerAlbumPropio(idAlbum, u.getUsuario());
    if (r.hasErrors()) {
      m.addAttribute("album", album);
      return "photo-form";
    }
    try {
      fotoService.agregarFotosAAlbumPropio(
        album,
        u.getUsuario(),
        f.getFotos(),
        f.getComentariosDeFotos()
      );
      a.addFlashAttribute("mensaje", "Fotos agregadas correctamente.");
      return "redirect:/albums/" + idAlbum;
    } catch (RuntimeException e) {
      r.reject("fotos", e.getMessage());
      m.addAttribute("album", album);
      return "photo-form";
    }
  }

  @PostMapping("/delete")
  public String eliminar(
    @PathVariable Long idAlbum,
    @RequestParam(required = false) Set<Long> idsFotos,
    @AuthenticationPrincipal CustomUserDetails u,
    RedirectAttributes a
  ) {
    try {
      fotoService.eliminarFotosDelAlbum(
        albumService.obtenerAlbumPropio(idAlbum, u.getUsuario()),
        idsFotos
      );
      a.addFlashAttribute("mensaje", "Fotos eliminadas correctamente.");
    } catch (RuntimeException e) {
      a.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/albums/" + idAlbum;
  }

  @GetMapping("/{idFoto}")
  public String detalle(
    @PathVariable Long idAlbum,
    @PathVariable Long idFoto,
    @AuthenticationPrincipal CustomUserDetails u,
    Model m
  ) {
    Album album = albumService.obtenerAlbumPropio(idAlbum, u.getUsuario());
    Foto foto = fotoService.obtenerFotoDelAlbum(idFoto, album);
    m.addAttribute("album", album);
    m.addAttribute("foto", foto);
    m.addAttribute(
      "comentarios",
      comentarioService.listarComentariosDeFoto(foto)
    );
    m.addAttribute("comentarioForm", new ComentarioForm());
    return "photo-detail";
  }

  @GetMapping("/{idFoto}/content")
  @ResponseBody
  public ResponseEntity<byte[]> contenido(
    @PathVariable Long idAlbum,
    @PathVariable Long idFoto,
    @AuthenticationPrincipal CustomUserDetails u
  ) {
    Foto foto = fotoService.obtenerFotoDelAlbum(
      idFoto,
      albumService.obtenerAlbumPropio(idAlbum, u.getUsuario())
    );
    return ResponseEntity.ok()
      .header("Content-Type", foto.getContentType())
      .body(foto.getContenido());
  }
}
