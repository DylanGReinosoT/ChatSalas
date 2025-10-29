package espe.edu.ec.SalasChat.repository;

import espe.edu.ec.SalasChat.model.Sala;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, UUID> {
}
