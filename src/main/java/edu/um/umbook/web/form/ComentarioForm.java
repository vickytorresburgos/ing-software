package edu.um.umbook.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComentarioForm {
    @NotBlank(message = "El comentario no puede estar vacío.")
    @Size(max = 250, message = "El comentario no puede superar 250 caracteres.")
    private String contenido;
}
