package teste.junior.finance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import teste.junior.finance.dto.categoria.CategoriaDtos.Request;
import teste.junior.finance.dto.categoria.CategoriaDtos.Response;
import teste.junior.finance.service.CategoriaService;

@RestController @RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }
    @GetMapping public List<Response> list() { return service.list(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Response create(@Valid @RequestBody Request r) { return service.create(r); }
    @PutMapping("/{id}") public Response update(@PathVariable Long id, @Valid @RequestBody Request r) { return service.update(id, r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}