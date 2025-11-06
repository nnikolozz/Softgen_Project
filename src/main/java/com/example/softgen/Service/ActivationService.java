package com.example.softgen.Service;

import com.example.softgen.Model.User;
import com.example.softgen.Repository.UserRepository;
import com.example.softgen.dto.ActivationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActivationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ActivationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void activateAccount(ActivationRequest req) {
        if (req.getToken() == null || req.getPassword() == null || req.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
        }

        User user = userRepository.findByEmailAndActivationToken(req.getEmail(), req.getToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid token or email"));

        if (user.isActivate()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Account already activated");
        }

        if (!isStrongPassword(req.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Weak password");
        }

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setActivate(true);
        user.setActivationToken(null);

        userRepository.save(user);
    }

    private boolean isStrongPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$");
    }
}
