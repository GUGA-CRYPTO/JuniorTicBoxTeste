import { NgIf } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { DashboardResumo } from '../../core/models/models';
import { DashboardService } from '../../core/services/dashboard.service';
import { CurrencyBrlPipe } from '../../shared/pipes/currency-brl.pipe';
import { friendlyError } from '../../shared/utils/error-message';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CurrencyBrlPipe, NgIf],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private readonly servico = inject(DashboardService);
  readonly carregando = signal(true);
  readonly erro = signal('');
  readonly resumo = signal<DashboardResumo | null>(null);
  readonly saudacao = signal(new Date().getHours() < 12 ? 'BOM DIA' : 'BOA TARDE');

  ngOnInit(): void {
    this.servico.resumo().subscribe({
      next: (dados) => { this.resumo.set(dados); this.carregando.set(false); },
      error: (erro) => { this.erro.set(friendlyError(erro)); this.carregando.set(false); },
    });
  }

  alturaBarra(valor: number, dados: DashboardResumo): number {
    const maximo = Math.max(dados.receitas, dados.despesas, 1);
    return Math.max((valor / maximo) * 100, 5);
  }
}
