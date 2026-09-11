import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DashboardResumo } from '../models/models';

interface DashboardApiResponse {
	receitas?: number;
	despesas?: number;
	saldo?: number;
	totalReceitas?: number;
	totalDespesas?: number;
	saldoLiquido?: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
	constructor(private readonly clienteHttp: HttpClient) {}

	resumo(): Observable<DashboardResumo> {
		return this.clienteHttp.get<DashboardApiResponse>(`${environment.apiUrl}/api/dashboard/resumo`).pipe(
			map((data) => ({
				receitas: data.receitas ?? data.totalReceitas ?? 0,
				despesas: data.despesas ?? data.totalDespesas ?? 0,
				saldo: data.saldo ?? data.saldoLiquido ?? 0
			}))
		);
	}
}
