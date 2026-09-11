
```markdown
# FolhaVerde 🌱 - Mini SaaS de Gestão Financeira

> Projeto full-stack desenvolvido como resolução de desafio técnico para vaga de Desenvolvedor Júnior, focado em boas práticas, isolamento de dados e arquitetura limpa.

## 📋 Sobre o Projeto

O **FolhaVerde** é um Mini SaaS (Software as a Service) voltado para a gestão financeira pessoal. Ele permite que os usuários criem suas contas, gerenciem suas categorias personalizadas e registrem receitas e despesas. O principal foco do sistema é o **isolamento rigoroso de dados**, garantindo que as informações de um usuário jamais sejam acessadas por outro.

## 🚀 Tecnologias Utilizadas

### Backend
* **Java & Spring Boot 3:** Base sólida e atualizada para a API REST.
* **Spring Security & JWT:** Autenticação stateless, protegendo os endpoints.
* **Spring Data JPA & Hibernate:** Mapeamento objeto-relacional e persistência.
* **MySQL:** Banco de dados relacional confiável e amplamente utilizado.
* **Lombok:** Redução de boilerplate code.

### Frontend
* **Angular 20 (Standalone):** Framework moderno sem NgModules.
* **Angular Signals & RxJS:** Gerenciamento de estado reativo e eficiente.
* **Reactive Forms:** Validação de formulários no lado do cliente.
* **SCSS:** Estilização modularizada e responsiva.

## ⚙️ Principais Decisões de Arquitetura

1. **Isolamento de Dados (Multitenancy lógico):** Todas as consultas no banco de dados para `Categoria` e `Transacao` são atreladas ao `usuario_id` extraído diretamente do token JWT no momento da requisição, prevenindo vulnerabilidades de injeção direta de ID (IDOR).
2. **DTOs (Data Transfer Objects):** Nenhuma entidade do banco de dados é exposta diretamente nas respostas HTTP, evitando vazamento de dados sensíveis e desacoplando a camada de persistência da camada de apresentação.
3. **Tratamento Global de Erros:** Implementação do `GlobalExceptionHandler` para interceptar exceções (ex: `BusinessException`, `ResourceNotFoundException`) e padronizar as respostas em JSON.
4. **Paginação (Pageable):** Para garantir o desempenho em escala (pensando em milhares de transações), a listagem de lançamentos utiliza a interface `Pageable` nativa do Spring, integrada aos controles de página no Angular.

## 🛠️ Como Executar o Projeto

### Pré-requisitos
Certifique-se de ter instalado em seu ambiente:
* **Java 17** ou superior
* **Node.js 22** ou superior
* **MySQL** (Servidor rodando localmente)
* **Git**

### Passo 1: Banco de Dados (MySQL Local)
Certifique-se de que o serviço do MySQL está rodando na sua máquina.
Abra sua ferramenta de banco de dados favorita (como MySQL Workbench, DBeaver ou o próprio terminal) e crie o banco de dados do projeto rodando o seguinte comando SQL:
```sql
CREATE DATABASE finance_db;

```

*Atenção:* Lembre-se de abrir o arquivo `src/main/resources/application.properties` no projeto do backend e verificar se as credenciais (`spring.datasource.username` e `spring.datasource.password`) correspondem ao seu usuário e senha do MySQL local. O Spring Boot se encarregará de criar as tabelas automaticamente.

### Passo 2: Rodando o Backend (API Spring Boot)

Abra o projeto do backend no seu editor ou terminal e execute a aplicação:

```bash
./mvnw spring-boot:run

```

*(No Windows, caso o comando acima não funcione, utilize `mvnw spring-boot:run`)*

*A API estará rodando em `http://localhost:8080*`

### Passo 3: Rodando o Frontend (Angular)

Abra o projeto frontend (`financeAngular`) em uma janela de terminal separada, instale as dependências e inicie o servidor:

```bash
npm install
npm start

```

*O painel web estará disponível e abrirá automaticamente em `http://localhost:4200*`

## 🧑‍💻 Autor

**Gustavo Santos Ferreira**

Estudante de Análise e Desenvolvimento de Sistemas.

*(Projeto desenvolvido para processo seletivo).*

```

```