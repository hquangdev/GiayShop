package edu.hq.furniture_shop.Repository;

import edu.hq.furniture_shop.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByEmailAndPassword(String email, String password);
}
