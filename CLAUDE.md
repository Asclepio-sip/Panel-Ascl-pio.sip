# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## O projeto

Asclépio é um backend SaaS multi-tenant (multi-empresa/multi-loja) de **gestão de estoque e catálogo online de produtos**: cadastro de produtos e variações, estoque por loja, movimentação/auditoria de estoque, pedidos (com baixa automática de estoque e geração de PDF), categorias, clientes, e administração de usuários/papéis/permissões.

Stack: Java 21, Spring Boot 3.5 (Web, Data JPA, Security, Validation), PostgreSQL, JWT (Auth0 `java-jwt`), Cloudflare R2 (S3-compatible, via AWS SDK v2) para upload de imagens, springdoc-openapi (Swagger), openhtmltopdf para geração de PDF de pedidos.

## Comandos

Build/execução usam o Maven Wrapper (não depende de Maven instalado globalmente).

```bash
./mvnw clean package -DskipTests   # build (é o que o Dockerfile usa)
./mvnw spring-boot:run             # rodar localmente
./mvnw test                        # rodar todos os testes
./mvnw test -Dtest=NomeDaClasse    # rodar uma única classe de teste
```

A aplicação exige variáveis de ambiente para subir (não há defaults de banco): `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `PASSWORD_PEPPER`, `R2_ACCESS_KEY`, `R2_SECRET_KEY`, `R2_BUCKET`, `R2_ENDPOINT`, `R2_PUBLIC_URL`. `JWT_SECRET` tem default (`my-secreat-key`) — trocar em produção.

`docker-compose.yml` sobe apenas o Postgres local (`postgres_login`, porta 5432, banco `login`), inicializado com `docker/init.sql`. O `Dockerfile` builda a aplicação com o wrapper e roda o jar gerado em `target/`.

`spring.jpa.hibernate.ddl-auto=update` — o schema é migrado automaticamente pelo Hibernate a partir das entidades, não há Flyway/Liquibase.

Não há linter/formatter configurado (sem Checkstyle/Spotless no `pom.xml`) — siga o estilo já presente no pacote que estiver editando.

## Arquitetura

Pacote raiz `Asclepio`, organizado por **domínio de negócio** (não por camada técnica), com naming case inconsistente entre módulos (`Controller` vs `controller`, `Api`/`api`/`API`) — verifique o pacote existente antes de criar um novo arquivo, e siga a convenção já usada naquele módulo específico.

Cada domínio segue tipicamente o mesmo padrão:
- **Entidade JPA** na raiz do pacote do domínio (ex.: `Produto/Produto.java`).
- **`Repository`** (Spring Data JPA) + uma classe **`*Specification`** estática (JPA Criteria) para filtros dinâmicos combináveis usados em listagens paginadas.
- **`Service`** com a lógica de negócio; alguns domínios maiores quebram isso em múltiplos services (ex. `Estoque/service/EstoqueQueryService`, `EstoqueMovimentacaoService`, `EstoqueValidator`; `Pedido/Service/PedidoEstoqueService`, `PedidoAtendimentoService`, `PedidoCodigoService`, `PedidoValidator`, `PedidoQueryService`).
- **`Controller` + interface `*Api`**: o controller (`@RestController`) implementa uma interface anotada com Swagger (`@Tag`, `@Operation`) e com as regras de autorização (`@PreAuthorize("hasAuthority('...')")`) — a doc e a regra de acesso do endpoint vivem na interface, a implementação fica enxuta.
- **`dto`**: records de request/response; listagens paginadas retornam `Page<T>` filtrado por um `*Filtro` (query params via `@ParameterObject`) + `Pageable`.

Fluxos que cruzam domínios ficam em um service dedicado que injeta os services dos domínios envolvidos, em vez de acoplar os domínios entre si (ex.: `CadastroProduto/CadastroProdutoService` orquestra `Produto` + `ProdutoVariacao` + `Estoque` num cadastro único transacional; `Pedido/Service/PedidoEstoqueService` faz a baixa de estoque de um pedido).

### Multi-tenancy (Empresa → Loja → Usuário)

- `Empresa` (tenant) tem várias `Loja`; `User` acessa lojas via `UserLoja` (N:N), cada vínculo carregando uma `Role` (que agrega `Permission`s).
- O JWT (`TokenService`) carrega `empresaId`, `lojaId` e a lista de `permissions` como claims — é o próprio token que define o contexto do tenant, não algo resolvido por request.
- Existem dois tipos de token: `TEMP` (curta duração, emitido no login antes do usuário escolher a loja quando ele tem acesso a mais de uma — só serve para chamar `/user/escolher-loja`) e `FULL` (contém `empresaId`/`lojaId`/`permissions`, é o token de uso normal). `SecurityFilter` trata os dois fluxos de forma diferente.
- `EmpresaContext` (request-scoped via `SecurityContextHolder`) expõe `getEmpresaId()`/`getLojaId()`/`getEmpresa()` — é assim que services/specifications pegam o tenant atual (ver uso em `EstoqueSpecification.filtrar`, que sempre filtra por `loja.empresa.id`).
- Autorização é por **permissão nominal** (`@PreAuthorize("hasAuthority('NomeDaPermissao')")`), não por role fixa. As permissões existentes são seedadas em `config/bootstrap/DataInitializer` — ao adicionar uma autorização nova em um endpoint, é preciso garantir que a permissão correspondente exista lá (e nas roles padrão criadas por `ServiceRole`).
- `DataInitializer` também garante em todo boot uma empresa/loja/usuário "Suporte" (idempotente, via `findOrElseGet`) — não é dado de teste, é bootstrap de sistema.

### Estoque e auditoria de movimentação

- `Estoque` é por par (loja, variação de produto). Toda alteração de estoque (criação, atualização manual, exclusão, promoção, baixa por pedido) gera um registro em `MovimentacaoEstoque` via `EstoqueMovimentacaoService`, guardando quantidade/preço/desconto antes-e-depois — é o log de auditoria do módulo de estoque, não deve ser contornado ao alterar estoque por um caminho novo.
- Baixa de estoque por pedido é feita em `PedidoEstoqueService.baixarEstoqueDoPedido`, transacional, lançando `BusinessException` se a variação não tiver estoque na loja.

### Erros e exceções

`GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza a tradução de exceções em `ErrorResponse`: `ResourceNotFoundException` → 404, `BusinessException` → 400, `ApiExternaException` → status customizado (erros de integração externa, ex. storage), qualquer outra `Exception` → 500. Lance a exceção de domínio apropriada em vez de devolver `ResponseEntity` de erro manualmente nos controllers/services.

### Autenticação de senha

Senhas usam `PepperedPasswordEncoder` (`config/security`): concatena um pepper vindo de `PASSWORD_PEPPER` antes de delegar pro `PasswordEncoder` real (BCrypt) — pepper é segredo de aplicação (env var), diferente do salt do BCrypt.

## Evitar erro

### Nome de arquivo `.java` precisa bater com o nome da classe (maiúsculas/minúsculas)

O projeto fica num disco **exFAT** (`/run/media/mateus/Arquivo`), que **não diferencia maiúsculas de minúsculas**. Um arquivo salvo como `CONTROLLERUSUARIO.JAVA` continua abrindo normalmente no editor e no Git parece o mesmo arquivo, mas o Maven/IntelliJ só compilam arquivos terminados em `.java` minúsculo. O arquivo é **ignorado em silêncio**: `./mvnw compile` dá `BUILD SUCCESS`, a app sobe, mas a classe simplesmente não existe em `target/classes`.

Já aconteceu com `Usuario/User/Controller/ControllerUsuario.java`: sem o controller, nenhuma rota `/user` era registrada e toda chamada (listagem, login, `PUT /user/{id}/lojas`...) retornava:

```json
{ "status": 500, "message": "No static resource user.", "path": "/user" }
```

Regras:
- Ao criar/editar/renomear um arquivo Java, use exatamente o nome da classe pública com extensão `.java` minúsculo (ex.: `ControllerUsuario.java`). Nunca gere nomes em caixa alta.
- `No static resource <rota>` = o Spring não achou **nenhum** controller para a rota. Antes de mexer em código, confira se o `.class` do controller existe: `find target/classes -name "NomeDoController.class"`.
- Para achar arquivos com extensão em caixa errada: `find src -name "*.JAVA" -o -name "*.Java"`.
- Para corrigir o nome no exFAT, renomeie em dois passos (renomear direto só mudando a caixa não funciona): `mv ARQUIVO.JAVA tmp && mv tmp Arquivo.java`, depois recompile e reinicie a aplicação.
