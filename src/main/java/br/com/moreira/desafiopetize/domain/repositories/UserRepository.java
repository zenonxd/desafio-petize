package br.com.moreira.desafiopetize.domain.repositories;

import br.com.moreira.desafiopetize.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(String username);
}
