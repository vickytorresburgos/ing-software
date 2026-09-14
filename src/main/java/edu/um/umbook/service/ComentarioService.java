package edu.um.umbook.service;

import edu.um.umbook.model.Comentario;
import edu.um.umbook.model.Foto;
import edu.um.umbook.model.Usuario;
import edu.um.umbook.repository.ComentarioRepository;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioService {

  private final ComentarioRepository comentarioRepository;

  public ComentarioService(ComentarioRepository comentarioRepository) {
    this.comentarioRepository = comentarioRepository;
  }

  @Transactional(readOnly = true)
  public List<Comentario> listarComentariosDeFoto(Foto foto) {
    return comentarioRepository.findByFotoOrderByFechaCreacionAsc(foto);
  }

  @Transactional
  public void guardarComentarioOpcional(
    String contenido,
    Foto foto,
    Usuario autor
  ) {
    if (contenido != null && !contenido.isBlank()) agregarComentario(
      contenido,
      foto,
      autor
    );
  }

  @Transactional
  public Comentario agregarComentario(
    String contenido,
    Foto foto,
    Usuario autor
  ) {
    validarContenido(contenido);
    return comentarioRepository.save(new Comentario(contenido, foto, autor));
  }

  @Transactional
  public Comentario modificarComentarioPropio(
    Long idComentario,
    Foto foto,
    Usuario autor,
    String contenido
  ) {
    validarContenido(contenido);
    Comentario comentario = comentarioRepository
      .findByIdAndFotoAndAutor(idComentario, foto, autor)
      .orElseThrow(() ->
        new IllegalArgumentException(
          "El comentario no existe, no pertenece a la foto o no es del usuario actual."
        )
      );
    comentario.modificarContenido(contenido);
    return comentario;
  }

  @Transactional
  public void eliminarComentarioDeFotoPropia(Long idComentario, Foto foto) {
    Comentario comentario = comentarioRepository
      .findByIdAndFoto(idComentario, foto)
      .orElseThrow(() ->
        new IllegalArgumentException(
          "El comentario no existe o no pertenece a la foto."
        )
      );
    comentarioRepository.delete(comentario);
  }

  @Transactional
  public void eliminarComentariosDeFotos(Collection<Foto> fotos) {
    if (!fotos.isEmpty()) comentarioRepository.deleteByFotoIn(fotos);
  }

  private void validarContenido(String contenido) {
    if (
      contenido == null || contenido.isBlank()
    ) throw new IllegalArgumentException("El comentario no puede estar vacío.");
    if (contenido.length() > 250) throw new IllegalArgumentException(
      "El comentario no puede superar 250 caracteres."
    );
  }
}
