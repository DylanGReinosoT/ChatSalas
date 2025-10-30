package espe.edu.ec.SalasChat.service;

import espe.edu.ec.SalasChat.model.Usuario;
import espe.edu.ec.SalasChat.repository.UsuarioRepository;
import espe.edu.ec.SalasChat.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    // 🔹 Registro de usuario
    public ResponseEntity<?> register(Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (repository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "El usuario ya existe"));
        }

        String hashedPassword = encoder.encode(password);
        Usuario user = new Usuario();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole("USER");

        repository.save(user);
        return ResponseEntity.ok(Map.of(
                "message", "Usuario registrado exitosamente",
                "username", username
        ));
    }

    // 🔹 Login de usuario
    public ResponseEntity<?> login(Map<String, String> cred) {
        var user = repository.findByUsername(cred.get("username")).orElse(null);

        if (user == null || !encoder.matches(cred.get("password"), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales inválidas"));
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
