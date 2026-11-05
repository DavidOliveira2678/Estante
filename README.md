# Estante

Desenvolvida para a cadeira de Desenvolvimento de Sistemas Web II do curso de Análise e Desenvolvimento de Sistemas do IFPE Campus Paulista, a Estante é uma rede social para leitores inspirada no Letterboxd. Nela, os usuários registram os livros que já leram, estão lendo ou desejam ler, avaliam com notas e resenhas, criam listas personalizadas e acompanham a atividade de outros leitores.


## Funcionalidades

- Cadastro e login (por **username ou e-mail**) com autenticação JWT
- Catálogo de livros com busca integrada ao **Google Books** (os livros encontrados são salvos no banco local)
- Estante pessoal com status de leitura: quero ler, lendo, lido e abandonado
- Resenhas com nota de 0,5 a 5 estrelas, curtidas e comentários
- Média de notas por livro
- Listas personalizadas, públicas ou privadas
- Seguir outros leitores e buscar usuários
- **Feed de atividades** dos leitores que você segue, alimentado por eventos no **Apache Kafka**

## Tecnologias

**Backend** (`backend/estante`)

- Java 21 e Spring Boot 4.1.1
- Spring Web MVC, Spring Data JPA, Spring Security e Bean Validation
- JWT (JJWT 0.13)
- Spring for Apache Kafka
- MySQL
- Lombok e Maven (wrapper incluído)

**Frontend** (`frontend/estante`)

- Vue 3, Vite, Pinia (estado), Vue Router e Axios

**Infraestrutura local:** Docker Compose, com MySQL e Kafka (`backend/estante/compose.yaml`).

## Arquitetura

O backend é, por enquanto, um monolito. O fluxo de atividades já é assíncrono:

```flowchart LR
    Vue["Vue.js"] -->|REST + JWT| API["Spring Boot"]
    API --> DB["MySQL (Banco de Dados)"]
    API -->|"resenha publicada / livro concluído"| K["Kafka: topico-atividades"]
    K --> C["Consumidor do feed"]
    C -->|"Activity + FeedItem por seguidor"| DB
    API -->|busca de livros| G["Google Books API"]
```

1. Ao publicar uma resenha ou marcar um livro como lido, o serviço dispara um evento interno.
2. Depois do **commit** da transação, o evento é publicado no tópico `topico-atividades`.
3. O consumidor registra a `Activity` e cria um `FeedItem` para cada seguidor do autor da ação.
4. O feed de cada usuário é consultado em `GET /api/feed`.

A publicação só acontece depois do commit, então um evento nunca descreve algo que acabou desfeito.

## Estrutura do repositório

```
Estante/
├── backend/estante/          # API Spring Boot
│   ├── compose.yaml          
│   └── src/main/java/br/com/estante/
│       ├── controller/       
│       ├── service/          
│       ├── repository/     
│       ├── entity/           
│       ├── dto/              
│       ├── security/         
│       ├── kafka/ event/ config/  
│       └── exception/        
└── frontend/estante/         # aplicação Vue
    └── src/
        ├── layouts/          
        ├── components/       
        ├── stores/           
        ├── services/api.js   
        ├── router/           
        └── styles/
```

## Como executar

### Pré-requisitos

- JDK 21
- Node.js (versão LTS recente) e npm
- Docker e Docker Compose

### 1. Subir o MySQL e o Kafka

```bash
cd backend/estante
docker compose up -d
```

O `compose.yaml` cria o banco `mydatabase` (usuário `myuser`, senha `secret`) na porta 3306 e um Kafka de nó único na porta 9092. São credenciais de desenvolvimento.

### 2. Configurar o `.env`

O Spring lê o `.env` a partir do **diretório de trabalho** da execução. Se você roda pela IDE a partir da raiz do repositório, ele fica em `Estante/.env`. Se roda pelo terminal em `backend/estante`, coloque uma cópia lá. O arquivo está no `.gitignore` e **nunca deve ser commitado**.

```properties
DB_URL=jdbc:mysql://localhost:3306/mydatabase
DB_USERNAME=myuser
DB_PASSWORD=secret

JWT_SECRET=gere_um_valor_aleatorio_com_pelo_menos_32_caracteres
JWT_EXPIRATION_MS=3600000

CORS_ALLOWED_ORIGINS=http://localhost:5173

# Opcionais
GOOGLE_BOOKS_API_KEY=
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

Gere o segredo do JWT com:

```bash
openssl rand -base64 48
```

| Variável | Descrição |
|---|---|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Conexão com o banco |
| `JWT_SECRET` | Segredo de assinatura dos tokens (mínimo de 32 caracteres) |
| `JWT_EXPIRATION_MS` | Validade do token em milissegundos (3600000 = 1 hora) |
| `CORS_ALLOWED_ORIGINS` | Origens do frontend permitidas, separadas por vírgula (padrão: `http://localhost:5173`) |
| `GOOGLE_BOOKS_API_KEY` | Chave do Google Books. Opcional: sem ela a busca usa a cota anônima |
| `KAFKA_BOOTSTRAP_SERVERS` | Endereço do Kafka (padrão: `localhost:9092`) |

### 3. Subir o backend

```bash
cd backend/estante
./mvnw spring-boot:run
```

A API fica em `http://localhost:8080`. As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`), o que serve para desenvolvimento, mas não para produção. Com o Kafka desligado, a API funciona, mas as atividades não chegam ao feed.

### 4. Subir o frontend

```bash
cd frontend/estante
npm install
npm run dev
```

A aplicação abre em `http://localhost:5173`. O endereço da API está fixo em `src/services/api.js` (`http://localhost:8080`).

### 5. Criar o primeiro administrador

O cadastro sempre cria usuários com o papel `USER`. Para promover alguém a `ADMIN`:

```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'seu_usuario';
```

Não é preciso gerar novo token: o papel é lido do banco a cada requisição.

## Frontend

| Rota | Página | Exige login |
|---|---|---|
| `/` | Home | Não |
| `/login`, `/cadastro` | Login e cadastro | Não |
| `/catalogo` | Catálogo e busca de livros | Não |
| `/livro/:id` | Detalhes do livro e resenhas | Não |
| `/estante` | Minha estante | Sim |
| `/social` | Feed, amigos e busca de usuários | Sim |
| `/perfil` | Configurações da conta | Sim |

O token JWT é guardado no `localStorage` e enviado em cada requisição por um interceptor do Axios. O guarda de rotas redireciona para `/login` quando a página exige autenticação.

## API

Todas as rotas têm o prefixo `/api`. A autenticação usa o header:

```
Authorization: Bearer <token>
```

O token é obtido em `POST /api/auth/login` e vale 1 hora.

### Permissões

| Acesso | Rotas |
|---|---|
| Público | `/api/auth/**` e as leituras (GET) de livros, autores, gêneros, resenhas, usuários e listas |
| Usuário logado | escrita em geral, `/api/me`, `/api/library`, `/api/feed`, `GET /api/reviews/me` |
| ADMIN | criar gênero; editar e apagar livros, autores e gêneros |

### Endpoints

| Recurso | Rotas |
|---|---|
| Autenticação | `POST /api/auth/register`, `POST /api/auth/login` |
| Perfil logado | `GET` e `PUT /api/me` |
| Usuários | `GET /api/users?q=` (busca), `GET /api/users/{id}`, `/username/{username}`, `/{id}/reviews`, `/{id}/library`, `/{id}/lists`, `/{id}/followers`, `/{id}/following`; `GET`, `POST` e `DELETE /{id}/follow` |
| Livros | `GET /api/books?title=` (paginado), `GET /{id}`, `/{id}/rating`, `/{id}/reviews`; `POST`, `PUT`, `DELETE` |
| Autores | CRUD em `/api/authors` (busca com `?name=`) |
| Gêneros | CRUD em `/api/genres` |
| Estante | `GET /api/library?status=`, `POST /api/library`, `GET`, `PUT` e `DELETE /api/library/{bookId}` |
| Resenhas | `POST /api/reviews`, `GET /me?bookId=`, `GET`, `PUT` e `DELETE /{id}` |
| Curtidas | `POST` e `DELETE /api/reviews/{id}/likes`, `GET /likes/count`, `GET /likes/me` |
| Comentários | `GET` e `POST /api/reviews/{id}/comments`, `DELETE /api/comments/{id}` |
| Listas | `POST /api/lists`, `GET`, `PUT` e `DELETE /{id}`, `POST /{id}/items`, `DELETE /{id}/items/{itemId}` |
| Feed | `GET /api/feed` (paginado, mais recentes primeiro) |

Os endpoints paginados aceitam `page`, `size` e `sort`. A primeira página é a `0`. Rotas protegidas respondem **403**.

### Busca de livros

`GET /api/books?title=...` consulta o **Google Books**, salva no banco os livros e autores ainda desconhecidos (identificados por origem e `externalId`) e devolve os resultados. Se o Google não responder ou não encontrar nada, a busca cai para o catálogo local.

## Modelo de dados

| Módulo | Entidades |
|---|---|
| Usuários | `User`, `Follow` |
| Catálogo | `Book`, `Author`, `Genre` |
| Estante | `UserBook` |
| Avaliações | `Review`, `ReviewLike`, `ReviewComment`, `BookRatingStats` |
| Listas | `BookList`, `BookListItem` |
| Feed | `Activity`, `FeedItem` |
| Notificações (planejado) | `Notification` |

A nota de uma resenha é um inteiro de 1 a 10, que representa de 0,5 a 5 estrelas. A média do livro em `BookRatingStats` é guardada em estrelas (0,50 a 5,00). Os módulos se referenciam por ID (`userId`, `bookId`...), sem chaves estrangeiras entre eles, o que facilita a futura separação em microsserviços.
