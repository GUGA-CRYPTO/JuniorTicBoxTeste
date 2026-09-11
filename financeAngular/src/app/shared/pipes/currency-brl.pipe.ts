import { Pipe, PipeTransform } from '@angular/core';
@Pipe({ name: 'currencyBrl', standalone: true })
export class CurrencyBrlPipe implements PipeTransform { transform(value: number): string { return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value); } }
