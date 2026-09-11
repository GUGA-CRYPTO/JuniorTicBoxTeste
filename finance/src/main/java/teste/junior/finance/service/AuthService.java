package teste.junior.finance.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teste.junior.finance.dto.auth.AuthDtos.*;
import teste.junior.finance.exception.BusinessException;
import teste.junior.finance.model.Usuario;
import teste.junior.finance.repository.UsuarioRepository;
import teste.junior.finance.security.JwtService;

@Service
public class AuthService {
    private final UsuarioRepository usuarios; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthService(UsuarioRepository usuarios, PasswordEncoder encoder, JwtService jwt) { this.usuarios = usuarios; this.encoder = encoder; this.jwt = jwt; }
    public AuthResponse register(RegisterRequest request) { if (usuarios.existsByEmail(request.email())) throw new BusinessException("E-mail já cadastrado"); Usuario u = usuarios.save(new Usuario(request.email().toLowerCase(), encoder.encode(request.senha()))); return new AuthResponse(jwt.generateToken(u.getId(), u.getEmail()), u.getId(), u.getEmail()); }
    public AuthResponse login(LoginRequest request) { Usuario u = usuarios.findByEmail(request.email().toLowerCase()).orElseThrow(() -> new BusinessException("Credenciais inválidas")); if (!encoder.matches(request.senha(), u.getSenha())) throw new BusinessException("Credenciais inválidas"); return new AuthResponse(jwt.generateToken(u.getId(), u.getEmail()), u.getId(), u.getEmail()); }
}