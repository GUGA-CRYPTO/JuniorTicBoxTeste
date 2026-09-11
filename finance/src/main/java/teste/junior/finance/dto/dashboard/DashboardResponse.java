package teste.junior.finance.dto.dashboard;

import java.math.BigDecimal;

public record DashboardResponse(BigDecimal receitas, BigDecimal despesas, BigDecimal saldo) {}