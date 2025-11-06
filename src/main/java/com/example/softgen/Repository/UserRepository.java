package com.example.softgen.Repository;

import com.example.softgen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndActivationToken(String email, String token);
    Optional<User> findByEmail(String email);

}
