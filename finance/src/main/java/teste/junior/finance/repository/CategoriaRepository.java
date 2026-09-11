package teste.junior.finance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import teste.junior.finance.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByUsuarioIdOrderByNome(Long usuarioId);
    Optional<Categoria> findByIdAndUsuarioId(Long id, Long usuarioId);
    boolean existsByNomeAndUsuarioId(String nome, Long usuarioId);
}