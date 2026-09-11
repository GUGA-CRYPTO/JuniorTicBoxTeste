package teste.junior.finance.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teste.junior.finance.exception.ResourceNotFoundException;
import teste.junior.finance.model.Usuario;
import teste.junior.finance.repository.UsuarioRepository;

@Service
public class CurrentUserService {
    private final UsuarioRepository repository;
    public CurrentUserService(UsuarioRepository repository) { this.repository = repository; }
    public Usuario get() { String email = SecurityContextHolder.getContext().getAuthentication().getName(); return repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado")); }
}