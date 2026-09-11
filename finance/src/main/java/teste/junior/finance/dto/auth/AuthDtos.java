package teste.junior.finance.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
    private AuthDtos() {}
    public record RegisterRequest(@NotBlank @Email String email, @NotBlank @Size(min = 6) String senha) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String senha) {}
    public record AuthResponse(String token, Long usuarioId, String email) {}
}