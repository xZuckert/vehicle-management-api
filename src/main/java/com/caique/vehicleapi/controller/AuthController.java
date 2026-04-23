package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.dto.AuthResponse;
import com.caique.vehicleapi.dto.LoginRequest;
import com.caique.vehicleapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(),
                        req.password()
                )
        );

        String token = jwtService.generateToken((UserDetails) auth.getPrincipal());

        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }
}