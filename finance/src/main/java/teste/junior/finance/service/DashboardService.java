package teste.junior.finance.service;

import org.springframework.stereotype.Service;
import teste.junior.finance.dto.dashboard.DashboardResponse;
import teste.junior.finance.model.TipoTransacao;
import teste.junior.finance.repository.TransacaoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DashboardService {
    private final TransacaoRepository transacoes; private final CurrentUserService currentUser;
    public DashboardService(TransacaoRepository transacoes, CurrentUserService currentUser) { this.transacoes = transacoes; this.currentUser = currentUser; }
    public DashboardResponse resumo() { var today = LocalDate.now(); var inicio = today.withDayOfMonth(1); var fim = today.withDayOfMonth(today.lengthOfMonth()); var id = currentUser.get().getId(); BigDecimal receitas = total(id, TipoTransacao.RECEITA, inicio, fim); BigDecimal despesas = total(id, TipoTransacao.DESPESA, inicio, fim); return new DashboardResponse(receitas, despesas, receitas.subtract(despesas)); }
    private BigDecimal total(Long id, TipoTransacao tipo, LocalDate inicio, LocalDate fim) { return transacoes.sumByUsuarioAndTipo(id, tipo, inicio, fim); }
}