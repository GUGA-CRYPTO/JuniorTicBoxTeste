package teste.junior.finance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teste.junior.finance.model.TipoTransacao;
import teste.junior.finance.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @EntityGraph(attributePaths = "categoria")
Page<Transacao> findByUsuarioIdOrderByDataDesc(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = "categoria")
    Optional<Transacao> findByIdAndUsuarioId(Long id, Long usuarioId);

    boolean existsByCategoriaIdAndUsuarioId(Long categoriaId, Long usuarioId);

    @Query("select t from Transacao t where t.usuario.id = :usuarioId and t.data between :inicio and :fim order by t.data desc")
    List<Transacao> findByUsuarioAndPeriod(@Param("usuarioId") Long usuarioId, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("select coalesce(sum(t.valor), 0) from Transacao t where t.usuario.id = :usuarioId and t.tipo = :tipo and t.data between :inicio and :fim")
    java.math.BigDecimal sumByUsuarioAndTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoTransacao tipo, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
