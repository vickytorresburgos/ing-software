package edu.um.umbook.pattern.state;

import edu.um.umbook.model.SolicitudAmistad;

public class SolicitudAmistadStateContext {
    private SolicitudState state;
    
    public SolicitudAmistadStateContext(SolicitudAmistad solicitud) {
        if (solicitud.getEstado() == null || solicitud.getEstado().equals("PENDING")) {
            this.state = new PendingState();
        } else if (solicitud.getEstado().equals("ACCEPTED")) {
            this.state = new AcceptedState();
        } else {
            this.state = new RejectedState();
        }
    }
    
    public void setState(SolicitudState state) {
        this.state = state;
    }
    
    public void handle(SolicitudAmistad solicitud) {
        state.handleRequest(solicitud, this);
    }
}
