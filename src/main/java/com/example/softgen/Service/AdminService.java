package com.example.softgen.Service;

import com.example.softgen.Model.Admin;
import com.example.softgen.Repository.AdminRepository;
import com.example.softgen.dto.AdminLoginRequest;
import com.example.softgen.dto.AuthResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Admin createAdmin(String firstname, String lastname, String email, String password) {
        if (adminRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        Admin admin = new Admin();
        admin.setFirstname(firstname);
        admin.setLastName(lastname);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));

        return adminRepository.save(admin);
    }

    public AuthResponse loginAdmin(AdminLoginRequest adminLoginRequest) {
        Admin admin = adminRepository.findByEmail(adminLoginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(adminLoginRequest.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String token = jwtService.generateToken(admin);
        return new AuthResponse("Login successful", token);
    }
}
