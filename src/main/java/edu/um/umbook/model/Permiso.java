package edu.um.umbook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long objetoId; // ID of Album or Wall User
    private String tipoObjeto; // "ALBUM" or "MURO"

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private GrupoAmigos grupo;

    @Enumerated(EnumType.STRING)
    private TipoPermiso permiso;
}
