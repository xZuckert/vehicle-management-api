package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.dto.AuthResponse;
import com.caique.vehicleapi.dto.LoginRequest;
import com.caique.vehicleapi.exception.BadRequestException;
import com.caique.vehicleapi.exception.UnauthorizedException;
import com.caique.vehicleapi.model.AppUser;
import com.caique.vehicleapi.repository.UserRepository;
import com.caique.vehicleapi.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.username(),
                            req.password()
                    )
            );

            String token = jwtService.generateToken((UserDetails) auth.getPrincipal());

            return ResponseEntity.ok(new AuthResponse(token, "Bearer"));

        } catch (Exception e) {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid LoginRequest req) {

        if (repo.findByUsername(req.username()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));
        user.setRoles(List.of("ROLE_USER"));

        repo.save(user);

        return ResponseEntity.ok().build();
    }
}