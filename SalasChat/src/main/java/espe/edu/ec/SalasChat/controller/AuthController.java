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
        var admin = repository.findByUsername(cred.get("<username")).orElse(null);
        if (admin == null || !encoder.matches(cred.get("password"), admin.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credneciales invalidas");
        }
        String token = jwtUtil.generateToken(admin.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
