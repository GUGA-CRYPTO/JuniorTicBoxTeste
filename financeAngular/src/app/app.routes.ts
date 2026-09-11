import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { ShellComponent } from './layout/shell.component';
import { LoginComponent } from './features/auth/login.component';
import { RegisterComponent } from './features/auth/register.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { TransactionsComponent } from './features/transacoes/transactions.component';
import { CategoriesComponent } from './features/categorias/categories.component';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'dashboard' },
	{ path: 'login', component: LoginComponent },
	{ path: 'cadastro', component: RegisterComponent },
	{ path: '', component: ShellComponent, canActivate: [authGuard], children: [
		{ path: 'dashboard', component: DashboardComponent },
		{ path: 'transacoes', component: TransactionsComponent },
		{ path: 'categorias', component: CategoriesComponent }
	]},
	{ path: '**', redirectTo: 'dashboard' }
];
