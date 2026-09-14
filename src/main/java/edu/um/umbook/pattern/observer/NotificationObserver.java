package edu.um.umbook.pattern.observer;

import edu.um.umbook.model.Usuario;

public interface NotificationObserver {
    void update(Usuario destinatario, String mensaje);
}
