package espe.edu.ec.SalasChat.service;

import espe.edu.ec.SalasChat.model.Administrador;
import espe.edu.ec.SalasChat.repository.AdministradorRepository;
import espe.edu.ec.SalasChat.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdministradorRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    // Registro de un nuevo administrador

    public ResponseEntity<?> register(Map<String, String> body){
        String username = body.get("username");
        String password = body.get("password");

        if(repository.findByUsername(username).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message","El usuario ya existe"));
        }

        String hashedPassword = encoder.encode(password);
        Administrador admin = new Administrador();
        admin.setUsername(username);
        admin.setPassword(hashedPassword);
        admin.setRole("ADMIN");

        repository.save(admin);
        return ResponseEntity.ok(Map.of(
                "message", "Administrador registrado exitosamente",
                "username", username
        ));
    }

    // 🔹 Login de administrador
    public ResponseEntity<?> login(Map<String, String> cred) {
        var admin = repository.findByUsername(cred.get("username")).orElse(null);

        if (admin == null || !encoder.matches(cred.get("password"), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales inválidas"));
        }

        String token = jwtUtil.generateToken(admin.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

}
