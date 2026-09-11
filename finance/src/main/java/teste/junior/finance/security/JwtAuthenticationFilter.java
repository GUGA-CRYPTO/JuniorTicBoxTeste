package teste.junior.finance.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import teste.junior.finance.repository.UsuarioRepository;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) { this.jwtService = jwtService; this.usuarioRepository = usuarioRepository; }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtService.isValid(token)) {
                usuarioRepository.findByEmail(jwtService.extractEmail(token)).ifPresent(usuario -> {
                    var principal = User.withUsername(usuario.getEmail()).password(usuario.getSenha()).authorities("USER").build();
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
                });
            }
        }
        chain.doFilter(request, response);
    }
}