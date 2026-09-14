package edu.um.umbook.pattern.state;

import edu.um.umbook.model.SolicitudAmistad;

public interface SolicitudState {
    void handleRequest(SolicitudAmistad solicitud, SolicitudAmistadStateContext context);
}
