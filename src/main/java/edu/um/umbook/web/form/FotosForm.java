package edu.um.umbook.web.form;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FotosForm {
    private List<MultipartFile> fotos = new ArrayList<>();
    private List<@Size(max = 250, message = "El comentario no puede superar 250 caracteres.") String> comentariosDeFotos = new ArrayList<>();
}
