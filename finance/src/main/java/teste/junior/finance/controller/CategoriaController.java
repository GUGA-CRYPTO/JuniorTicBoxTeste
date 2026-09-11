package teste.junior.finance.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import teste.junior.finance.dto.categoria.CategoriaDtos.*;
import teste.junior.finance.service.CategoriaService;
import java.util.List;

@RestController @RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }
    @GetMapping public List<Response> list() { return service.list(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Response create(@Valid @RequestBody Request r) { return service.create(r); }
    @PutMapping("/{id}") public Response update(@PathVariable Long id, @Valid @RequestBody Request r) { return service.update(id, r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}