import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Categoria, Page, TipoTransacao, Transacao } from '../../core/models/models';
import { CategoryService } from '../../core/services/category.service';
import { TransactionService } from '../../core/services/transaction.service';
import { CurrencyBrlPipe } from '../../shared/pipes/currency-brl.pipe';
import { DateBrPipe } from '../../shared/pipes/date-br.pipe';
import { friendlyError } from '../../shared/utils/error-message';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgFor, CurrencyBrlPipe, DateBrPipe],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.scss',
})
export class TransactionsComponent implements OnInit {
  private readonly servico = inject(TransactionService);
  private readonly servicoCategorias = inject(CategoryService);
  private readonly construtorFormulario = inject(FormBuilder);

  readonly transacoesPage = signal<Page<Transacao> | null>(null);
  paginaAtual = 0;
  readonly categorias = signal<Categoria[]>([]);
  readonly carregando = signal(true);
  readonly editando = signal(false);
  readonly idEmEdicao = signal<number | null>(null);
  readonly erro = signal('');
  readonly sucesso = signal('');

  readonly formulario = this.construtorFormulario.nonNullable.group({
    descricao: ['', [Validators.required]],
    valor: [0, [Validators.required, Validators.min(0.01)]],
    data: ['', [Validators.required]],
    tipo: ['DESPESA' as TipoTransacao, [Validators.required]],
    categoriaId: [0, [Validators.required, Validators.min(1)]],
  });
  busca = '';
  filtro = 'TODOS';
  ordenacao = 'data';

  ngOnInit(): void {
    this.carregar();
    this.servicoCategorias.list().subscribe({
      next: (dados) => this.categorias.set(dados),
      error: (erro) => this.erro.set(friendlyError(erro)),
    });
  }

  carregar(pagina: number = 0): void {
    this.carregando.set(true);
    this.paginaAtual = pagina;
    this.servico.list(pagina, 10).subscribe({
      next: (dados) => {
        this.transacoesPage.set(dados);
        this.carregando.set(false);
      },
      error: (erro) => {
        this.erro.set(friendlyError(erro));
        this.carregando.set(false);
      },
    });
  }

  proximaPagina(): void {
    const page = this.transacoesPage();
    if (page && !page.last) {
      this.carregar(this.paginaAtual + 1);
    }
  }

  paginaAnterior(): void {
    const page = this.transacoesPage();
    if (page && !page.first) {
      this.carregar(this.paginaAtual - 1);
    }
  }

  transacoesFiltradas(): Transacao[] {
    const termo = this.busca.toLowerCase();
    const transacoes = this.transacoesPage()?.content ?? [];
    return transacoes
      .filter(
        (item) =>
          (this.filtro === 'TODOS' || item.tipo === this.filtro) &&
          (item.descricao.toLowerCase().includes(termo) ||
            item.categoriaNome.toLowerCase().includes(termo)),
      )
      .sort((a, b) =>
        this.ordenacao === 'valor' ? b.valor - a.valor : b.data.localeCompare(a.data),
      );
  }

  iniciarNovo(): void {
    this.idEmEdicao.set(null);
    this.formulario.reset({
      descricao: '',
      valor: 0,
      data: new Date().toISOString().slice(0, 10),
      tipo: 'DESPESA',
      categoriaId: 0,
    });
    this.editando.set(true);
  }

  editar(transacao: Transacao): void {
    this.idEmEdicao.set(transacao.id);
    this.formulario.setValue({
      descricao: transacao.descricao,
      valor: transacao.valor,
      data: transacao.data,
      tipo: transacao.tipo,
      categoriaId: transacao.categoriaId,
    });
    this.editando.set(true);
  }

  cancelar(): void {
    this.editando.set(false);
  }

  salvar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }
    const dados = this.formulario.getRawValue();
    const requisicao = this.idEmEdicao()
      ? this.servico.update(this.idEmEdicao()!, dados)
      : this.servico.create(dados);
    requisicao.subscribe({
      next: () => {
        this.sucesso.set('Transação salva com sucesso.');
        this.cancelar();
        this.carregar();
      },
      error: (erro) => this.erro.set(friendlyError(erro)),
    });
  }

  remover(transacao: Transacao): void {
    if (!confirm(`Excluir “${transacao.descricao}”?`)) return;
    this.servico.remove(transacao.id).subscribe({
      next: () => {
        this.sucesso.set('Transação excluída.');
        this.carregar();
      },
      error: (erro) => this.erro.set(friendlyError(erro)),
    });
  }

  rastrearPorId(_: number, item: Transacao): number {
    return item.id;
  }
}

