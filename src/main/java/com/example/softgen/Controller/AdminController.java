package com.example.softgen.Controller;

import com.example.softgen.Model.Admin;
import com.example.softgen.Service.AdminService;
import com.example.softgen.Service.JwtService;
import com.example.softgen.dto.AdminLoginRequest;
import com.example.softgen.dto.AdminSignupRequest;
import com.example.softgen.dto.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminController {

    private final AdminService adminService;
    private final JwtService jwtService;

    public AdminController(AdminService adminService, JwtService jwtService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminSignupRequest adminSignupRequest) {
        Admin admin = adminService.createAdmin(
                adminSignupRequest.getFirstname(),
                adminSignupRequest.getLastname(),
                adminSignupRequest.getEmail(),
                adminSignupRequest.getPassword()
        );
        String token = jwtService.generateToken(admin);
        return ResponseEntity.ok(new AuthResponse("Admin registered successfully", token));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginAdmin(@Valid @RequestBody AdminLoginRequest request) {
        AuthResponse response = adminService.loginAdmin(request);
        return ResponseEntity.ok(response);
    }
}

