package edu.um.umbook.controller;

import edu.um.umbook.model.Album;
import edu.um.umbook.repository.GrupoAmigosRepository;
import edu.um.umbook.security.CustomUserDetails;
import edu.um.umbook.service.AlbumService;
import edu.um.umbook.service.FotoService;
import edu.um.umbook.web.form.AlbumForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/albums")
public class AlbumController {

  private final AlbumService albumService;
  private final FotoService fotoService;
  private final GrupoAmigosRepository grupoRepository;

  public AlbumController(
    AlbumService albumService,
    FotoService fotoService,
    GrupoAmigosRepository grupoRepository
  ) {
    this.albumService = albumService;
    this.fotoService = fotoService;
    this.grupoRepository = grupoRepository;
  }

  @GetMapping
  public String listar(@AuthenticationPrincipal CustomUserDetails u, Model m) {
    m.addAttribute(
      "albumes",
      albumService.listarAlbumesPropios(u.getUsuario())
    );
    return "albums";
  }

  @GetMapping("/new")
  public String nuevo(@AuthenticationPrincipal CustomUserDetails u, Model m) {
    cargarFormulario(m, u, new AlbumForm());
    return "album-form";
  }

  @PostMapping
  public String crear(
    @AuthenticationPrincipal CustomUserDetails u,
    @Valid @ModelAttribute("albumForm") AlbumForm f,
    BindingResult r,
    Model m,
    RedirectAttributes a
  ) {
    if (r.hasErrors()) {
      cargarFormulario(m, u, f);
      return "album-form";
    }
    try {
      Album album = albumService.crearAlbum(
        u.getUsuario(),
        f.getNombre(),
        f.getDescripcion(),
        f.getGruposVisualizacionIds(),
        f.getGruposComentarioIds(),
        f.getFotos(),
        f.getComentariosDeFotos()
      );
      a.addFlashAttribute("mensaje", "Álbum creado correctamente.");
      return "redirect:/albums/" + album.getId();
    } catch (RuntimeException e) {
      r.reject("album", e.getMessage());
      cargarFormulario(m, u, f);
      return "album-form";
    }
  }

  @GetMapping("/{id}")
  public String detalle(
    @PathVariable Long id,
    @AuthenticationPrincipal CustomUserDetails u,
    Model m
  ) {
    Album album = albumService.obtenerAlbumPropio(id, u.getUsuario());
    m.addAttribute("album", album);
    m.addAttribute("fotos", fotoService.listarFotosDelAlbum(album));
    return "album-detail";
  }

  @PostMapping("/{id}/delete")
  public String eliminar(
    @PathVariable Long id,
    @AuthenticationPrincipal CustomUserDetails u,
    RedirectAttributes a
  ) {
    try {
      albumService.eliminarAlbumPropio(id, u.getUsuario());
      a.addFlashAttribute("mensaje", "Álbum eliminado correctamente.");
    } catch (RuntimeException e) {
      a.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/albums";
  }

  private void cargarFormulario(Model m, CustomUserDetails u, AlbumForm f) {
    m.addAttribute("albumForm", f);
    m.addAttribute("grupos", grupoRepository.findByUsuario(u.getUsuario()));
  }
}
