import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return next(request).pipe(catchError((error: HttpErrorResponse) => {
    if ((error.status === 401 || error.status === 403) && !request.url.includes('/auth/')) {
      auth.clearSession();
      void router.navigate(['/login'], { queryParams: { motivo: 'sessao' } });
    }
    return throwError(() => error);
  }));
};
