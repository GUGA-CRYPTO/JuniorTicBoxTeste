import { Pipe, PipeTransform } from '@angular/core';
@Pipe({ name: 'dateBr', standalone: true })
export class DateBrPipe implements PipeTransform { transform(value: string): string { return new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(new Date(`${value}T12:00:00Z`)); } }
