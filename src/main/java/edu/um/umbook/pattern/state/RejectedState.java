package edu.um.umbook.pattern.state;

import edu.um.umbook.model.SolicitudAmistad;

public class RejectedState implements SolicitudState {
    @Override
    public void handleRequest(SolicitudAmistad solicitud, SolicitudAmistadStateContext context) {
        solicitud.setEstado("REJECTED");
    }
}
