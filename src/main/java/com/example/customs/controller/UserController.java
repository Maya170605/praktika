package com.example.customs.controller;

import com.example.customs.dto.UserDTO;
import com.example.customs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO dto) {
        UserDTO saved = userService.register(dto);
        return ResponseEntity.ok(saved);
    }
}
