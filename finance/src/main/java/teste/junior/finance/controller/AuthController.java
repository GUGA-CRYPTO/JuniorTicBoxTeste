package teste.junior.finance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import teste.junior.finance.dto.auth.AuthDtos.*;
import teste.junior.finance.dto.auth.AuthDtos.AuthResponse;
import teste.junior.finance.dto.auth.AuthDtos.LoginRequest;
import teste.junior.finance.dto.auth.AuthDtos.RegisterRequest;
import teste.junior.finance.service.AuthService;

@RestController @RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public AuthResponse register(@Valid @RequestBody RegisterRequest r) { return service.register(r); }
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest r) { return service.login(r); }
}