package espe.edu.ec.SalasChat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import java.util.UUID;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue
    private UUID id;

    private String nombre;
    private String tipo;

    @Column(nullable = false)
    private String pinHash;

    private Instant createdAt = Instant.now();

}
