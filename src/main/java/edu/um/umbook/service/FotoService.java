package edu.um.umbook.service;

import edu.um.umbook.exception.FormatoDeFotoInvalidoException;
import edu.um.umbook.exception.FotosNoSeleccionadasException;
import edu.um.umbook.model.Album;
import edu.um.umbook.model.Foto;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.repository.FotoRepository;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {

  private final FotoRepository fotoRepository;
  private final ComentarioService comentarioService;

  public FotoService(
    FotoRepository fotoRepository,
    ComentarioService comentarioService
  ) {
    this.fotoRepository = fotoRepository;
    this.comentarioService = comentarioService;
  }

  @Transactional
  public void guardarFotos(
    Album album,
    Usuario autor,
    List<MultipartFile> archivos,
    List<String> comentarios
  ) {
    if (archivos == null) return;
    try {
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        for (int indice = 0; indice < archivos.size(); indice++) {
          MultipartFile archivo = archivos.get(indice);
          if (archivo == null || archivo.isEmpty()) continue;
          
            byte[] contenido = archivo.getBytes();
            if (!esJpeg(archivo.getContentType(), contenido)) {
                throw new FormatoDeFotoInvalidoException("Solo se permiten imágenes JPEG.");
            }
            String uniqueName = UUID.randomUUID().toString() + ".jpg";
            Path filePath = uploadDir.resolve(uniqueName);
            Files.write(filePath, contenido);
            
            Foto foto = new Foto(filePath.toString(), archivo.getOriginalFilename(), MediaType.IMAGE_JPEG_VALUE, album);
            fotoRepository.save(foto);
            comentarioService.guardarComentarioOpcional(
              obtenerComentario(comentarios, indice),
              foto,
              autor
            );
        }
    } catch (IOException exception) {
        throw new IllegalStateException("No se pudo leer la foto.", exception);
    }
  }

  @Transactional
  public void agregarFotosAAlbumPropio(
    Album album,
    Usuario autor,
    List<MultipartFile> archivos,
    List<String> comentarios
  ) {
    if (
      archivos == null ||
      archivos
        .stream()
        .allMatch(archivo -> archivo == null || archivo.isEmpty())
    ) throw new FotosNoSeleccionadasException();
    guardarFotos(album, autor, archivos, comentarios);
  }

  @Transactional(readOnly = true)
  public List<Foto> listarFotosDelAlbum(Album album) {
    return fotoRepository.findByAlbum(album);
  }

  @Transactional(readOnly = true)
  public Foto obtenerFotoDelAlbum(Long idFoto, Album album) {
    return fotoRepository
      .findByIdAndAlbum(idFoto, album)
      .orElseThrow(() ->
        new IllegalArgumentException(
          "La foto no existe o no pertenece al álbum."
        )
      );
  }

  @Transactional
  public void eliminarFotosDelAlbum(Album album, Set<Long> idsFotos) {
    if (
      idsFotos == null || idsFotos.isEmpty()
    ) throw new FotosNoSeleccionadasException();
    List<Foto> fotos = fotoRepository.findByIdInAndAlbum(idsFotos, album);
    if (fotos.size() != idsFotos.size()) throw new IllegalArgumentException(
      "Una o más fotos no pertenecen al álbum."
    );
    eliminarFotos(fotos);
  }

  @Transactional
  public void eliminarTodasLasFotosDelAlbum(Album album) {
    eliminarFotos(fotoRepository.findByAlbum(album));
  }

  private void eliminarFotos(Collection<Foto> fotos) {
    if (!fotos.isEmpty()) {
      comentarioService.eliminarComentariosDeFotos(fotos);
      fotoRepository.deleteAll(fotos);
    }
  }

  private String obtenerComentario(List<String> comentarios, int indice) {
    return comentarios != null && indice < comentarios.size()
      ? comentarios.get(indice)
      : null;
  }

  private boolean esJpeg(String contentType, byte[] contenido) {
    return (
      MediaType.IMAGE_JPEG_VALUE.equals(contentType) &&
      contenido.length >= 4 &&
      (contenido[0] & 0xFF) == 0xFF &&
      (contenido[1] & 0xFF) == 0xD8 &&
      (contenido[contenido.length - 2] & 0xFF) == 0xFF &&
      (contenido[contenido.length - 1] & 0xFF) == 0xD9
    );
  }
}
