import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly chaveArmazenamento = 'finance-session';
  readonly sessao = signal<AuthResponse | null>(this.lerSessao());
  constructor(
    private readonly clienteHttp: HttpClient,
    private readonly router: Router,
  ) {}
  login(request: LoginRequest): Observable<AuthResponse> {
    return this.clienteHttp
      .post<AuthResponse>(`${environment.apiUrl}/auth/login`, request)
      .pipe(tap((response) => this.salvarSessao(response)));
  }
  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.clienteHttp
      .post<AuthResponse>(`${environment.apiUrl}/auth/register`, request)
      .pipe(tap((response) => this.salvarSessao(response)));
  }
  token(): string | null {
    return this.sessao()?.token ?? null;
  }
  isAuthenticated(): boolean {
    return !!this.token();
  }
  logout(): void {
    localStorage.removeItem(this.chaveArmazenamento);
    this.sessao.set(null);
    void this.router.navigate(['/login']);
  }
  clearSession(): void {
    localStorage.removeItem(this.chaveArmazenamento);
    this.sessao.set(null);
  }
  private salvarSessao(response: AuthResponse): void {
    localStorage.setItem(this.chaveArmazenamento, JSON.stringify(response));
    this.sessao.set(response);
  }
  private lerSessao(): AuthResponse | null {
    try {
      const raw = localStorage.getItem(this.chaveArmazenamento);
      return raw ? (JSON.parse(raw) as AuthResponse) : null;
    } catch {
      return null;
    }
  }
}
