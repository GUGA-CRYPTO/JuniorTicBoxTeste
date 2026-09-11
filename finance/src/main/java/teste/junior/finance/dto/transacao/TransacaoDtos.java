package teste.junior.finance.dto.transacao;

import jakarta.validation.constraints.*;
import teste.junior.finance.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class TransacaoDtos {
    private TransacaoDtos() {}
    public record Request(@NotBlank String descricao, @NotNull @Positive BigDecimal valor, @NotNull LocalDate data,
                          @NotNull TipoTransacao tipo, @NotNull Long categoriaId) {}
    public record Response(Long id, String descricao, BigDecimal valor, LocalDate data, TipoTransacao tipo,
                           Long categoriaId, String categoriaNome) {
        public static Response from(Transacao t) { return new Response(t.getId(), t.getDescricao(), t.getValor(), t.getData(), t.getTipo(), t.getCategoria().getId(), t.getCategoria().getNome()); }
    }
}