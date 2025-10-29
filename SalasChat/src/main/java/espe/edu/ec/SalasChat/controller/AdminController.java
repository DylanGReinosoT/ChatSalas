package espe.edu.ec.SalasChat.controller;

import espe.edu.ec.SalasChat.model.Sala;
import espe.edu.ec.SalasChat.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final SalaRepository salaRepository;
    private final PasswordEncoder encoder;

    @PostMapping("/salas")
    public ResponseEntity<Sala> crearSala(@RequestBody Map<String, String> body){
        Sala sala = new  Sala();
        sala.setNombre(body.get("nombre"));
        sala.setPinHash(encoder.encode(body.get("pin")));
        Sala guardada = salaRepository.save(sala);
        return ResponseEntity.ok(guardada);
    }
}
