package teste.junior.finance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import teste.junior.finance.dto.categoria.CategoriaDtos.*;
import teste.junior.finance.dto.categoria.CategoriaDtos.Request;
import teste.junior.finance.dto.categoria.CategoriaDtos.Response;
import teste.junior.finance.exception.BusinessException;
import teste.junior.finance.exception.ConflictException;
import teste.junior.finance.exception.ResourceNotFoundException;
import teste.junior.finance.model.Categoria;
import teste.junior.finance.repository.CategoriaRepository;
import teste.junior.finance.repository.TransacaoRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository categorias; private final TransacaoRepository transacoes; private final CurrentUserService currentUser;
    public CategoriaService(CategoriaRepository categorias, TransacaoRepository transacoes, CurrentUserService currentUser) { this.categorias = categorias; this.transacoes = transacoes; this.currentUser = currentUser; }
    public List<Response> list() { return categorias.findByUsuarioIdOrderByNome(currentUser.get().getId()).stream().map(Response::from).toList(); }
    public Response create(Request request) { var user = currentUser.get(); if (categorias.existsByNomeAndUsuarioId(request.nome(), user.getId())) throw new BusinessException("Categoria já cadastrada"); return Response.from(categorias.save(new Categoria(request.nome(), user))); }
    public Response update(Long id, Request request) { var user = currentUser.get(); Categoria c = owned(id, user.getId()); c.setNome(request.nome()); return Response.from(categorias.save(c)); }
    public void delete(Long id) {
        var userId = currentUser.get().getId();
        var categoria = owned(id, userId);
        if (transacoes.existsByCategoriaIdAndUsuarioId(categoria.getId(), userId)) {
            throw new ConflictException("Categoria possui transações vinculadas");
        }
        categorias.delete(categoria);
    }
    public Categoria owned(Long id, Long userId) { return categorias.findByIdAndUsuarioId(id, userId).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada")); }
}