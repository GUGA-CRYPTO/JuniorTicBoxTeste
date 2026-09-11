package teste.junior.finance.controller;

import org.springframework.web.bind.annotation.*;
import teste.junior.finance.dto.dashboard.DashboardResponse;
import teste.junior.finance.service.DashboardService;

@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;
    public DashboardController(DashboardService service) { this.service = service; }
    @GetMapping("/resumo") public DashboardResponse resumo() { return service.resumo(); }
}