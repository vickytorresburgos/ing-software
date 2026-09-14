package edu.um.umbook.pattern.singleton;

import org.springframework.stereotype.Component;

@Component
public class AdministradorSistema {
    private static AdministradorSistema instance;
    
    private AdministradorSistema() {}
    
    public static synchronized AdministradorSistema getInstance() {
        if (instance == null) {
            instance = new AdministradorSistema();
        }
        return instance;
    }
    
    public void disableUser(Long userId) {
        // Logging or logic
    }
}
