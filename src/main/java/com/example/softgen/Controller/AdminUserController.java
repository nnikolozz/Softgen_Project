package com.example.softgen.Controller;


import com.example.softgen.Service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PostMapping("users")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> req) {
        String name = req.get("name");
        String surname = req.get("surname");
        String email = req.get("email");
        return ResponseEntity.ok(adminUserService.createUser(name, surname, email));
    }
}
