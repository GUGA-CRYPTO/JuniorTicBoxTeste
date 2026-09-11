import { NgIf } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { friendlyError } from '../../shared/utils/error-message';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private readonly construtorFormulario = inject(FormBuilder);
  private readonly roteador = inject(Router);
  private readonly servicoAutenticacao = inject(AuthService);
  readonly carregando = signal(false);
  readonly erro = signal('');
  readonly formulario = this.construtorFormulario.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    senha: ['', [Validators.required, Validators.minLength(6)]],
  });

  entrar(): void {
    if (this.formulario.invalid) { this.formulario.markAllAsTouched(); return; }
    this.carregando.set(true);
    this.erro.set('');
    this.servicoAutenticacao.login(this.formulario.getRawValue()).subscribe({
      next: () => void this.roteador.navigate(['/dashboard']),
      error: (erro) => { this.erro.set(friendlyError(erro, 'Não foi possível entrar.')); this.carregando.set(false); },
    });
  }
}
