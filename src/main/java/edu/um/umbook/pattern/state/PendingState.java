package edu.um.umbook.pattern.state;

import edu.um.umbook.model.SolicitudAmistad;

public class PendingState implements SolicitudState {
    @Override
    public void handleRequest(SolicitudAmistad solicitud, SolicitudAmistadStateContext context) {
        solicitud.setEstado("PENDING");
    }
}
