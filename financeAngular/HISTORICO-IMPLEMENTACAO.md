# Histórico de implementação e revisão

## Objetivo

Construir um frontend Angular moderno para controle financeiro pessoal, consumindo uma API REST em `http://localhost:8080`, com interface em português do Brasil.

O projeto atual está em `/home/guss/TesteJunior/financeAngular`.

## Stack utilizada

- Angular 20 standalone
- TypeScript strict
- Angular Router
- Reactive Forms
- Angular Signals
- HttpClient com interceptors
- RxJS
- SCSS
- Karma/Jasmine configurados
- Node.js 22

A aplicação não usa dados mockados para os fluxos principais. Dashboard, categorias e transações usam a API real.

## Estado inicial

O workspace estava vazio. A fundação foi criada com Angular CLI 20 porque o CLI mais recente exigia uma versão de Node ligeiramente superior à disponível no ambiente.

Comando utilizado:

```bash
npx --yes @angular/cli@20 new finance-angular --directory . --routing --style=scss --standalone --strict --skip-git --skip-tests --package-manager=npm --ssr=false --zoneless=false
```

## Arquitetura final

```text
src/
  app/
    app.config.ts
    app.routes.ts
    app.ts

    core/
      guards/
        auth.guard.ts
      interceptors/
        auth.interceptor.ts
        error.interceptor.ts
      models/
        models.ts
      services/
        auth.service.ts
        category.service.ts
        dashboard.service.ts
        transaction.service.ts

    features/
      auth/
        login.component.ts
        login.component.html
        login.component.scss
        register.component.ts
        register.component.html
        register.component.scss
      categorias/
        categories.component.ts
        categories.component.html
        categories.component.scss
      dashboard/
        dashboard.component.ts
        dashboard.component.html
        dashboard.component.scss
      transacoes/
        transactions.component.ts
        transactions.component.html
        transactions.component.scss

    layout/
      shell.component.ts
      shell.component.html
      shell.component.scss

    shared/
      pipes/
        currency-brl.pipe.ts
        date-br.pipe.ts
      utils/
        error-message.ts

  environments/
    environment.ts
```

Cada componente possui agora seu arquivo TypeScript, template HTML e estilos SCSS separados.

## Divisão das tarefas

### 1. Fundação Angular

- Criar o projeto standalone.
- Configurar Angular Router.
- Configurar `provideHttpClient`.
- Configurar interceptors.
- Criar environment e proxy.

### 2. Contratos e infraestrutura

- Criar interfaces de autenticação, categoria, transação, dashboard, erros e paginação.
- Criar `AuthService`.
- Criar `DashboardService`.
- Criar `CategoryService`.
- Criar `TransactionService`.
- Criar `AuthGuard`.
- Criar interceptor do JWT.
- Criar interceptor para 401/403.
- Criar utilitário de mensagens amigáveis.

### 3. Autenticação

- Criar tela de login.
- Criar tela de cadastro.
- Persistir sessão no navegador.
- Redirecionar para dashboard depois do login/cadastro.
- Limpar sessão no logout.

### 4. Área autenticada

- Criar shell com menu lateral.
- Exibir e-mail do usuário.
- Criar navegação para dashboard, transações e categorias.
- Implementar layout mobile.

### 5. Dashboard

- Consumir `/api/dashboard/resumo`.
- Exibir receitas, despesas e saldo.
- Formatar moeda brasileira.
- Destacar saldo negativo.
- Criar visualização gráfica simples usando os valores reais da API.
- Tratar loading, erro e estados vazios.

### 6. Categorias

- Listar categorias.
- Criar categoria.
- Editar categoria.
- Excluir categoria com confirmação.
- Atualizar lista após operações.
- Validar nome obrigatório e tamanho mínimo.

### 7. Transações

- Listar transações.
- Criar transação.
- Editar transação.
- Excluir transação com confirmação.
- Usar data, valor, tipo e categoria.
- Filtrar por tipo.
- Buscar por descrição ou categoria.
- Ordenar por data ou valor.
- Implementar paginação conforme o contrato atual da API.
- Impedir valores menores que `0,01`.

### 8. Identidade visual

O visual inicial tinha aparência genérica. Foi substituído por uma direção editorial:

- fundo em papel quente;
- textura de grade discreta;
- azul-marinho;
- coral;
- amarelo mostarda;
- tipografia `Fraunces`, `DM Sans` e `DM Mono`;
- bordas mais firmes;
- cards menos arredondados;
- sidebar com identidade própria;
- layout responsivo preservado.

### 9. Refatoração de código

Os componentes inicialmente tinham template e CSS inline dentro do `.ts`. Eles foram separados em `.ts`, `.html` e `.scss`.

Também foram traduzidos diversos identificadores internos:

- `loading` para `carregando`;
- `error` para `erro`;
- `success` para `sucesso`;
- `service` para `servico`;
- `form` para `formulario`;
- `editing` para `editando`;
- `search` para `busca`;
- `filter` para `filtro`;
- `sort` para `ordenacao`;
- `session` para `sessao`;
- `storageKey` para `chaveArmazenamento`;
- `http` para `clienteHttp`;
- `url` para `endereco`.

## Principais problemas encontrados e resolvidos

### CLI Angular incompatível com Node

O Angular CLI mais recente exigia Node `22.22.3` ou superior, enquanto o ambiente tinha Node `22.22.1`.

Solução: usar Angular CLI 20.

### Interceptors referenciados antes da criação

O bootstrap foi configurado para importar interceptors e guard antes de os arquivos existirem.

Solução: criar os arquivos do core e validar novamente com build.

### Diretivas standalone ausentes

Os templates usavam `*ngIf` e `*ngFor`, mas alguns componentes não importavam `NgIf` e `NgFor` no array `imports` do componente.

Sintoma: erros de runtime como `Can't bind to 'ngIf'`.

Solução: importar as diretivas nos componentes standalone.

### CORS durante o teste da API

O frontend usava inicialmente `http://localhost:8080` diretamente no navegador. Isso fazia a chamada ignorar o proxy Angular e falhar com CORS.

Solução:

- configurar `environment.apiUrl` como string vazia em desenvolvimento;
- manter chamadas relativas como `/api/...` e `/auth/...`;
- usar `proxy.conf.json` para encaminhar para `http://localhost:8080`.

### Contrato diferente no dashboard

A API respondeu primeiro com:

```json
{
  "totalReceitas": 0.0,
  "totalDespesas": 42.5,
  "saldoLiquido": -42.5
}
```

O frontend esperava `receitas`, `despesas` e `saldo`, produzindo `R$ NaN`.

Solução: `DashboardService` passou a normalizar ambos os formatos. Depois a API foi ajustada e passou a responder com o contrato padronizado:

```json
{
  "receitas": 0.0,
  "despesas": 42.5,
  "saldo": -42.5
}
```

### API retornando 500 em transações e categorias

Durante os primeiros testes:

- `GET /api/transacoes` retornava `500`;
- excluir categoria vinculada a transação retornava `500`.

Foi criado o arquivo [BACKEND_FIX_PROMPT.md](BACKEND_FIX_PROMPT.md) com evidências, respostas reais, hipóteses de investigação e checklist para a IA do backend.

Depois da correção do backend, os endpoints passaram a funcionar.

### Paginação

A API passou a retornar uma estrutura paginada usando `Pageable`, com campos como:

- `content`;
- `totalElements`;
- `totalPages`;
- `number`;
- `size`;
- `first`;
- `last`.

O frontend foi ajustado para usar `Page<Transacao>`, enviar `page` e `size`, e controlar os botões anterior/próxima.

### Validação visual incompleta

O formulário já bloqueava valores negativos por causa de `Validators.min(0.01)`, mas não mostrava uma mensagem para o campo valor.

Também não havia mensagem visual para data ou categoria inválida.

Correção realizada:

- mensagem para valor maior que zero;
- mensagem para data obrigatória;
- mensagem para categoria obrigatória;
- exibição de `Página 0 de 0` quando não há registros, evitando `Página 1 de 0`.

## Validações e testes executados

### Build

```bash
npm run build -- --configuration development
```

Resultado atual: passou.

Bundle gerado em `dist/finance-angular`.

### Diagnóstico do editor

```text
No errors found.
```

### Runner de testes

```bash
npm test -- --watch=false --browsers=ChromeHeadless
```

Resultado:

```text
TOTAL: 0 SUCCESS
```

Importante: o runner funciona, mas o projeto ainda não possui arquivos `*.spec.ts`. Portanto, nenhum caso automatizado foi executado.

### Servidor local

```bash
npm start
```

O servidor usa:

```text
http://localhost:4200
```

E o proxy encaminha `/api` e `/auth` para:

```text
http://localhost:8080
```

## Teste real no navegador

Foi aberto o frontend no navegador e foram testados os fluxos abaixo.

### Sem sessão

- `/login`: abriu corretamente.
- `/cadastro`: abriu corretamente.
- `/dashboard`: redirecionou para `/login`.
- `/transacoes`: redirecionou para `/login`.
- `/categorias`: redirecionou para `/login`.

### Login

Conta de teste usada anteriormente:

```text
finance.teste.20260909@example.com
```

O login redirecionou para `/dashboard` e a sessão foi reconhecida pelo shell.

### Dashboard autenticado

- Título carregado.
- Resumo carregado pela API.
- Cards renderizados.
- Nenhum aviso visual de erro.

### Transações autenticadas

- Tela carregada.
- Campo de busca carregado.
- Filtro por tipo carregado.
- Ordenação carregada.
- Paginação carregada.
- Modal de nova transação abriu.

### Validação de transação

Foi digitado valor `-5`.

Resultado:

- formulário ficou inválido;
- nenhum envio deveria ser executado;
- mensagem `Informe um valor maior que zero.` apareceu;
- mensagens de descrição e categoria também apareceram quando vazias.

### Categorias autenticadas

- Tela carregada.
- Lista e estado vazio renderizados.
- Nenhum erro visual detectado.

## Estado atual

### Funcionando

- compilação;
- servidor Angular;
- rotas públicas;
- proteção de rotas;
- login;
- persistência de sessão;
- interceptor JWT;
- dashboard;
- listagem paginada de transações;
- categorias;
- transações;
- validações básicas;
- layout responsivo;
- componentes separados em três arquivos;
- integração com API corrigida.

### Pendências técnicas

1. Criar testes unitários reais com Jasmine para serviços e componentes.
2. Cobrir `TransactionService` com `HttpTestingController`.
3. Cobrir `AuthService`, interceptor e guard.
4. Cobrir validações do formulário de transação.
5. Substituir `confirm()` nativo por modal reutilizável.
6. Evitar `$any` nos eventos HTML usando handlers tipados.
7. Criar componentes reutilizáveis para alertas, modal e estado de carregamento.
8. Avaliar persistência em cookie seguro ou estratégia equivalente caso o backend exija maior proteção contra XSS.
9. Validar fluxo mobile com teste visual automatizado.

## Como outra IA deve continuar

Antes de alterar código:

1. Ler este arquivo inteiro.
2. Ler `README.md`.
3. Verificar o contrato atual da API, especialmente paginação de transações.
4. Não reintroduzir templates inline.
5. Não substituir chamadas reais por mocks.
6. Rodar `npm run build -- --configuration development` antes e depois das mudanças.
7. Criar specs antes de refatorar regras de negócio.
8. Preservar a separação por `core`, `features`, `layout` e `shared`.
9. Verificar mudanças do usuário nos arquivos antes de editar.

## Comandos principais

```bash
npm install
npm start
npm run build -- --configuration development
npm test -- --watch=false --browsers=ChromeHeadless
```
