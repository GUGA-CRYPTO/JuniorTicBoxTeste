export interface AuthResponse {
  token: string;
  usuarioId: number;
  email: string;
}
export interface LoginRequest {
  email: string;
  senha: string;
}
export interface RegisterRequest {
  email: string;
  senha: string;
}
export interface Categoria {
  id: number;
  nome: string;
}
export interface CategoriaRequest {
  nome: string;
}
export type TipoTransacao = 'RECEITA' | 'DESPESA';
export interface Transacao {
  id: number;
  descricao: string;
  valor: number;
  data: string;
  tipo: TipoTransacao;
  categoriaId: number;
  categoriaNome: string;
}
export interface TransacaoRequest {
  descricao: string;
  valor: number;
  data: string;
  tipo: TipoTransacao;
  categoriaId: number;
}
export interface DashboardResumo {
  receitas: number;
  despesas: number;
  saldo: number;
}
export interface ApiError {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
}
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}