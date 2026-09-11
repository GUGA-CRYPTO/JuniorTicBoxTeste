import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transacao, TransacaoRequest } from '../models/models';
import { Page } from '../models/models';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  private readonly endereco = `${environment.apiUrl}/api/transacoes`;
  constructor(private readonly clienteHttp: HttpClient) {}

  list(page: number = 0, size: number = 10): Observable<Page<Transacao>> {
    return this.clienteHttp.get<Page<Transacao>>(`${this.endereco}?page=${page}&size=${size}`);
  }

  create(data: TransacaoRequest): Observable<Transacao> {
    return this.clienteHttp.post<Transacao>(this.endereco, data);
  }
  update(id: number, data: TransacaoRequest): Observable<Transacao> {
    return this.clienteHttp.put<Transacao>(`${this.endereco}/${id}`, data);
  }
  remove(id: number): Observable<void> {
    return this.clienteHttp.delete<void>(`${this.endereco}/${id}`);
  }
}
