package teste.junior.finance.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teste.junior.finance.dto.dashboard.DashboardResponse;
import teste.junior.finance.service.DashboardService;

@RestController @RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    private final DashboardService service;
    public DashboardController(DashboardService service) { this.service = service; }
    @GetMapping("/resumo") public DashboardResponse resumo() { return service.resumo(); }
}