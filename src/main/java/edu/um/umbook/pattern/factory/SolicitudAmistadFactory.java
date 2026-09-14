package edu.um.umbook.pattern.factory;

import edu.um.umbook.model.SolicitudAmistad;
import edu.um.umbook.model.Usuario;
import java.time.LocalDateTime;

public class SolicitudAmistadFactory {
    public static SolicitudAmistad createSolicitud(Usuario solicitante, Usuario receptor) {
        SolicitudAmistad solicitud = new SolicitudAmistad();
        solicitud.setSolicitante(solicitante);
        solicitud.setReceptor(receptor);
        solicitud.setEstado("PENDING");
        solicitud.setFechaCreacion(LocalDateTime.now());
        return solicitud;
    }
}
