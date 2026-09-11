package teste.junior.finance.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import teste.junior.finance.dto.transacao.TransacaoDtos.Request;
import teste.junior.finance.dto.transacao.TransacaoDtos.Response;
import teste.junior.finance.exception.ResourceNotFoundException;
import teste.junior.finance.model.Transacao;
import teste.junior.finance.repository.TransacaoRepository;

@Service
public class TransacaoService {

    private final TransacaoRepository transacoes;
    private final CategoriaService categorias;
    private final CurrentUserService currentUser;

    public TransacaoService(TransacaoRepository transacoes, CategoriaService categorias, CurrentUserService currentUser) {
        this.transacoes = transacoes;
        this.categorias = categorias;
        this.currentUser = currentUser;
    }

   @Transactional(readOnly = true)
public Page<Response> list(Pageable pageable) {
    return transacoes.findByUsuarioIdOrderByDataDesc(currentUser.get().getId(), pageable)
                     .map(Response::from);
}

    @Transactional
    public Response create(Request r) {
        var u = currentUser.get();
        var t = new Transacao();
        apply(t, r, u.getId());
        t.setUsuario(u);
        return Response.from(transacoes.save(t));
    }

    @Transactional
    public Response update(Long id, Request r) {
        var u = currentUser.get();
        var t = owned(id, u.getId());
        apply(t, r, u.getId());
        return Response.from(transacoes.save(t));
    }

    @Transactional
    public void delete(Long id) {
        transacoes.delete(owned(id, currentUser.get().getId()));
    }

    private void apply(Transacao t, Request r, Long userId) {
        t.setDescricao(r.descricao());
        t.setValor(r.valor());
        t.setData(r.data());
        t.setTipo(r.tipo());
        t.setCategoria(categorias.owned(r.categoriaId(), userId));
    }

    private Transacao owned(Long id, Long userId) {
        return transacoes.findByIdAndUsuarioId(id, userId).orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));
    }
}
