package edu.um.umbook.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AlbumForm {
    @NotBlank(message = "El nombre del álbum es obligatorio.")
    @Size(max = 30, message = "El nombre no puede superar 30 caracteres.")
    private String nombre;

    @Size(max = 250, message = "La descripción no puede superar 250 caracteres.")
    private String descripcion;

    private Set<Long> gruposVisualizacionIds = new HashSet<>();
    private Set<Long> gruposComentarioIds = new HashSet<>();
    private List<MultipartFile> fotos = new ArrayList<>();
    private List<@Size(max = 250, message = "El comentario no puede superar 250 caracteres.") String> comentariosDeFotos = new ArrayList<>();
}
