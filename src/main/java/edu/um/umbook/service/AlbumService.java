package edu.um.umbook.service;

import edu.um.umbook.model.Album;
import edu.um.umbook.model.GrupoAmigos;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.repository.AlbumRepository;
import edu.um.umbook.repository.GrupoAmigosRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlbumService {

  private final AlbumRepository albumRepository;
  private final GrupoAmigosRepository grupoRepository;
  private final FotoService fotoService;

  public AlbumService(
    AlbumRepository albumRepository,
    GrupoAmigosRepository grupoRepository,
    FotoService fotoService
  ) {
    this.albumRepository = albumRepository;
    this.grupoRepository = grupoRepository;
    this.fotoService = fotoService;
  }

  @Transactional
  public Album crearAlbum(
    Usuario creador,
    String nombre,
    String descripcion,
    Set<Long> gruposVisualizacionIds,
    Set<Long> gruposComentarioIds,
    List<MultipartFile> archivos,
    List<String> comentarios
  ) {
    Album album = new Album(nombre, descripcion, creador);
    album.asignarPermisos(
      obtenerGruposPropios(creador, gruposVisualizacionIds),
      obtenerGruposPropios(creador, gruposComentarioIds)
    );
    Album guardado = albumRepository.save(album);
    fotoService.guardarFotos(guardado, creador, archivos, comentarios);
    return guardado;
  }

  @Transactional(readOnly = true)
  public List<Album> listarAlbumesPropios(Usuario creador) {
    return albumRepository.findByCreador(creador);
  }

  @Transactional(readOnly = true)
  public Album obtenerAlbumPropio(Long idAlbum, Usuario creador) {
    return albumRepository
      .findByIdAndCreador(idAlbum, creador)
      .orElseThrow(() ->
        new IllegalArgumentException(
          "El álbum no existe o no pertenece al usuario actual."
        )
      );
  }

  @Transactional
  public void eliminarAlbumPropio(Long idAlbum, Usuario creador) {
    Album album = obtenerAlbumPropio(idAlbum, creador);
    fotoService.eliminarTodasLasFotosDelAlbum(album);
    albumRepository.delete(album);
  }

  private List<GrupoAmigos> obtenerGruposPropios(
    Usuario usuario,
    Set<Long> ids
  ) {
    if (ids == null || ids.isEmpty()) return Collections.emptyList();
    List<GrupoAmigos> grupos = grupoRepository
      .findByUsuario(usuario)
      .stream()
      .filter(grupo -> ids.contains(grupo.getId()))
      .toList();
    if (grupos.size() != ids.size()) throw new IllegalArgumentException(
      "Uno o más grupos no pertenecen al usuario actual."
    );
    return grupos;
  }
}
