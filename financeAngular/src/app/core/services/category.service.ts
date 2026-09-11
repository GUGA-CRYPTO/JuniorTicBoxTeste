import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Categoria, CategoriaRequest } from '../models/models';
@Injectable({ providedIn: 'root' })
export class CategoryService {
	private readonly endereco = `${environment.apiUrl}/api/categorias`;
	constructor(private readonly clienteHttp: HttpClient) {}
	list(): Observable<Categoria[]> { return this.clienteHttp.get<Categoria[]>(this.endereco); }
	create(data: CategoriaRequest): Observable<Categoria> { return this.clienteHttp.post<Categoria>(this.endereco, data); }
	update(id: number, data: CategoriaRequest): Observable<Categoria> { return this.clienteHttp.put<Categoria>(`${this.endereco}/${id}`, data); }
	remove(id: number): Observable<void> { return this.clienteHttp.delete<void>(`${this.endereco}/${id}`); }
}
