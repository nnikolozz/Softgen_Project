package com.example.softgen.Controller;


import com.example.softgen.Repository.UserRepository;
import com.example.softgen.Service.ActivationService;
import com.example.softgen.dto.ActivationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class ActivationController {
    private final ActivationService activationService;

    public ActivationController(ActivationService activationService) {
        this.activationService = activationService;
    }
@PostMapping("/activation")
    public ResponseEntity<Map<String,String>> activate(@RequestBody ActivationRequest activationRequest){
    activationService.activateAccount(activationRequest);
    return ResponseEntity.ok(Map.of("message", "Account activated successfully"));
}
}
