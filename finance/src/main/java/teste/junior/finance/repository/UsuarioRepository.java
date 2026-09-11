package teste.junior.finance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import teste.junior.finance.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}