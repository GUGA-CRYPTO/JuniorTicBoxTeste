package teste.junior.finance.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias", uniqueConstraints = @UniqueConstraint(columnNames = {"nome", "usuario_id"}))
@Getter @Setter @NoArgsConstructor
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Categoria(String nome, Usuario usuario) {
        this.nome = nome;
        this.usuario = usuario;
    }
}