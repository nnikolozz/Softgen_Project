package com.example.softgen.Service;

import com.example.softgen.Repository.UserRepository;
import com.example.softgen.dto.LoginRequest;
import com.example.softgen.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import com.example.softgen.Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserLoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }
        String token = jwtService.generateToken(user);
        return new LoginResponse(token, "Login successfully");
    }

}