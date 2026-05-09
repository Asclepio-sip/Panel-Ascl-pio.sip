# 🟢 Projeto Base de Telas e Login (Spring Boot + Angular)

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![Angular](https://img.shields.io/badge/Angular-16-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)

---

## Descrição do Projeto

Este projeto é uma **aplicação web full-stack** feita com **Java 21**, **Spring Boot** e **Angular**.  
Ele demonstra um sistema completo de **autenticação e autorização** com **três tipos de usuários** e **gestão de produtos**.

### Tipos de Usuários

1. **Visitante (sem login)**  
   - Acessa apenas a **parte pública** do site.  
   - Pode ver a **lista de produtos públicos**.  

2. **Usuário comum (USER)**  
   - Pode se **registrar e logar** no sistema.  
   - Pode **criar produtos** (ex.: adicionar produtos).  
   - Não pode deletar produtos nem criar usuários.  

3. **Administrador (ADMIN)**  
   - Pode **criar usuários** (novos admins ou usuários).  
   - Pode **criar, listar e deletar produtos**.  
   - Tem acesso completo ao sistema.

---

video do Projeto -> https://youtu.be/QNbRW_KyXCE

link do front -> https://github.com/mateus26guerra/front-projeto_base_de_telas_e_login
## Tecnologias Usadas


---
- **Backend:** Java 21, Spring Boot 3, Spring Security, JWT  
- **Frontend:** Angular 16+  
- **Banco de Dados:** PostgreSQL  
- **ORM:** Spring Data JPA  
- **Segurança:** JWT + BCrypt  
- **Documentação:** Swagger (OpenAPI)  

---

## Estrutura do Projeto
adapter
├─ in/web → Controllers e DTOs (entrada de dados)
├─ out/persistence → Repositórios e adapters para o banco de dados
domain
├─ model → Entidades do domínio (User, Product)
└─ usecase → Casos de uso (UserUseCase, ProdutoUseCase)
tudo
└─ security → Configurações de segurança (JWT, Filters)


---

## Funcionalidades

### 🔐 Autenticação

- **POST /auth/login** → Realiza login, retorna token JWT  
- **POST /auth/register** → Registra um novo usuário  

### 🧑‍💼 Usuários (apenas ADMIN)

- **GET /auth/admin/users** → Lista todos os usuários  
- **PUT /auth/admin/users/{id}** → Atualiza usuário  
- **POST /auth/register** → Cria usuário (ADMIN ou USER)

### 🛒 Produtos

- **GET /productsPublico/list** → Lista produtos públicos (qualquer usuário)  
- **GET /products/list** → Lista produtos (usuário logado)  
- **POST /products/add_products** → Cria produto (USER ou ADMIN)  
- **DELETE /products/{id}** → Deleta produto (apenas ADMIN)  

---

## Segurança

- **JWT Tokens** para autenticação  
- **BCrypt** para hash de senhas  
- **Roles:** `ROLE_USER`, `ROLE_ADMIN`  
- **Filtros de segurança** (`SecurityFilter`) protegem endpoints privados  
- **CORS** configurado para o frontend `http://localhost:4200`  

---

## Banco de Dados

- **PostgreSQL**  
- Tabelas principais:  
  - `users`: armazena usuários com UUID, username, password e role  
  - `product`: armazena produtos com id, nome, preço e imagem  

Exemplo de script inicial:

```sql
CREATE DATABASE login;
\c login
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$12$EXEMPLOHASHDEBCrypt', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    image_url TEXT
);
```


