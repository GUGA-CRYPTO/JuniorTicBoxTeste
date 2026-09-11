import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Categoria } from '../../core/models/models';
import { CategoryService } from '../../core/services/category.service';
import { friendlyError } from '../../shared/utils/error-message';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgFor],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss',
})
export class CategoriesComponent implements OnInit {
  private readonly servico = inject(CategoryService);
  private readonly construtorFormulario = inject(FormBuilder);
  readonly categorias = signal<Categoria[]>([]);
  readonly carregando = signal(true);
  readonly editando = signal(false);
  readonly idEmEdicao = signal<number | null>(null);
  readonly erro = signal('');
  readonly sucesso = signal('');
  readonly formulario = this.construtorFormulario.nonNullable.group({ nome: ['', [Validators.required, Validators.minLength(2)]] });

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.carregando.set(true);
    this.servico.list().subscribe({
      next: (dados) => { this.categorias.set(dados); this.carregando.set(false); },
      error: (erro) => { this.erro.set(friendlyError(erro)); this.carregando.set(false); },
    });
  }

  iniciarNova(): void { this.idEmEdicao.set(null); this.formulario.reset(); this.editando.set(true); }
  editar(categoria: Categoria): void { this.idEmEdicao.set(categoria.id); this.formulario.setValue({ nome: categoria.nome }); this.editando.set(true); }
  cancelar(): void { this.editando.set(false); this.formulario.reset(); }

  salvar(): void {
    if (this.formulario.invalid) { this.formulario.markAllAsTouched(); return; }
    const dados = this.formulario.getRawValue();
    const requisicao = this.idEmEdicao() ? this.servico.update(this.idEmEdicao()!, dados) : this.servico.create(dados);
    requisicao.subscribe({
      next: () => { this.sucesso.set('Categoria salva com sucesso.'); this.cancelar(); this.carregar(); },
      error: (erro) => this.erro.set(friendlyError(erro)),
    });
  }

  remover(categoria: Categoria): void {
    if (!confirm(`Excluir a categoria “${categoria.nome}”?`)) return;
    this.servico.remove(categoria.id).subscribe({
      next: () => { this.sucesso.set('Categoria excluída.'); this.carregar(); },
      error: (erro) => this.erro.set(friendlyError(erro)),
    });
  }

  rastrearPorId(_: number, item: Categoria): number { return item.id; }
}
