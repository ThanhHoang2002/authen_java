package PizzaHut_be.dao.repository;

import PizzaHut_be.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserModelRepository extends JpaRepository<Client, String> {
    Optional<Client> findByEmail(String email);

    @Query(value = "select * from client where email LIKE %?1% LIMIT 1", nativeQuery = true)
    Optional<Client> findOneByEmail(String email);
}
