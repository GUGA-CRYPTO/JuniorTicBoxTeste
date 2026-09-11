import { HttpErrorResponse } from '@angular/common/http';
import { ApiError } from '../../core/models/models';
export function friendlyError(error: unknown, fallback = 'Não foi possível concluir a operação.'): string {
  if (error instanceof HttpErrorResponse) { const body = error.error as ApiError | null; if (body?.message) return body.message; if (error.status === 0) return 'Não foi possível conectar à API. Verifique se o backend está ativo.'; if (error.status === 401) return 'E-mail ou senha inválidos.'; if (error.status >= 500) return 'O servidor encontrou um problema. Tente novamente.'; }
  return fallback;
}
