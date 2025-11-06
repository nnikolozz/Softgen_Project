package com.example.softgen.Service;


import com.example.softgen.Model.User;
import com.example.softgen.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public Map<String, Object> createUser(String firstname, String lastname, String email) {
        if (userRepository.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");

        User user = new User();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setActivationToken(UUID.randomUUID().toString());

        userRepository.save(user);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Activate your account");
        msg.setText("Click to activate: http://localhost:8080/api/v1/auth/activation");
        mailSender.send(msg);

        return Map.of(
                "message", "User created and activation email sent",
                "userId", user.getId(),
                "activationToken", user.getActivationToken()
        );
    }
}
