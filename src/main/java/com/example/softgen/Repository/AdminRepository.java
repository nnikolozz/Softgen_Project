package com.example.softgen.Repository;

import com.example.softgen.Model.Admin;
import com.example.softgen.dto.AdminSignupRequest;
import com.example.softgen.dto.AuthResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);


}
