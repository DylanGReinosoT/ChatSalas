package espe.edu.ec.SalasChat.controller;

import espe.edu.ec.SalasChat.repository.AdministradorRepository;
import espe.edu.ec.SalasChat.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AuthController {
    private final AdministradorRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> cred) {
        System.out.println("Intento de login: " + cred);

        var admin = repository.findByUsername(cred.get("username")).orElse(null);
        if (admin == null) {
            System.out.println("❌ Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        System.out.println("Usuario encontrado: " + admin.getUsername());
        System.out.println("Hash en BD: " + admin.getPassword());
        System.out.println("Coinciden?: " + encoder.matches(cred.get("password"), admin.getPassword()));

        if (!encoder.matches(cred.get("password"), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(admin.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

}
