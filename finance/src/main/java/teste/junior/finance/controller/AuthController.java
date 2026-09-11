package teste.junior.finance.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import teste.junior.finance.dto.auth.AuthDtos.*;
import teste.junior.finance.service.AuthService;

@RestController @RequestMapping("/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public AuthResponse register(@Valid @RequestBody RegisterRequest r) { return service.register(r); }
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest r) { return service.login(r); }
}