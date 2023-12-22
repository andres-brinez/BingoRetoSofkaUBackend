package com.softkaU.Reto.controller;

import com.softkaU.Reto.model.UserModel;
import com.softkaU.Reto.service.UserService;
import com.softkaU.Reto.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
public ResponseEntity<?> signup(@RequestBody UserModel user) {
        try {
            userService.findByUsername(user.getUsername());
            // Si no se lanzó una excepción, el nombre de usuario ya existe
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("El nombre de usuario ya existe");
        } catch (UsernameNotFoundException e) {
            // El nombre de usuario no existe, proceder a guardar el nuevo usuario
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UserModel savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        }
}

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserModel user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (Exception ex) {
            throw new RuntimeException("Invalid username/password");
        }

        final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("Token", jwt);

        return ResponseEntity.ok(response);
    }


}