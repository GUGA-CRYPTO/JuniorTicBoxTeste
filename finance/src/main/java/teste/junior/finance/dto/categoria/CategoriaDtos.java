package teste.junior.finance.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import teste.junior.finance.model.Categoria;

public final class CategoriaDtos {
    private CategoriaDtos() {}
    public record Request(@NotBlank String nome) {}
    public record Response(Long id, String nome) {
        public static Response from(Categoria c) { return new Response(c.getId(), c.getNome()); }
    }
}