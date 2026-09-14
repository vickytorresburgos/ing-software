package edu.um.umbook.config;

import edu.um.umbook.model.Usuario;
import edu.um.umbook.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Sistema");
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admin@umbook.edu.um");
                usuarioRepository.save(admin);
            }
            if(usuarioRepository.findByUsername("user").isEmpty()) {
                Usuario user = new Usuario();
                user.setNombre("User");
                user.setApellido("Demo");
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user"));
                user.setEmail("user@umbook.edu.um");
                usuarioRepository.save(user);
            }
            System.out.println("Data seeder completed.");
        };
    }
}
