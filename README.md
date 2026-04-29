# product-inventory-Project

Aplicação full stack para gerenciamento de produção.

O sistema permite cadastrar produtos, matérias-primas e definir a composição de cada produto. Com isso, é possível saber quantos produtos podem ser fabricados com base no estoque disponível.

---
# Apresentação do projeto

Assista ao vídeo demonstrando as funcionalidades e o design do sistema:

Link do video: https://youtu.be/k9xLXdRabyA
Link do LinkedIn: https://www.linkedin.com/feed/update/urn:li:activity:7432241056236064768/

---
## Back-end

O back-end é responsável pela regra de negócio e pela API.

Principais funções:

* CRUD de produtos
* CRUD de matérias-primas
* Definição da composição do produto (relação produto ↔ matéria-prima)
* Métricas para dashboard
* Cálculo de produtos fabricáveis
* Sugestão de produção baseada no estoque

## Front-end

O front consome a API e exibe as informações.

Possui:

* Dashboard com métricas
* Tela de produtos
* Tela de matérias-primas
* Tela de composição (product raw material)
* Modais para create/edit
* Modal de confirmação para delete

O objetivo foi criar uma interface simples, organizada e fácil de evoluir.

## Objetivo do projeto

Projeto criado para praticar arquitetura full stack, modelagem de dados e implementação de regra de negócio além de CRUD simples.


---


# Manual de instalação — product-inventory-Project

Guia rápido para rodar o projeto localmente.

---

## ✅ Pré-requisitos

Instale antes:

* Node.js (versão 18+)
* Java 17+
* Maven
* MySQL
* Git

---

## 📦 Back-end (Quarkus)

### 1. Clonar o projeto

```bash
git clone <repo>
cd backend
```

### 2. Configurar banco de dados

Criar o banco no MySQL:

```sql
CREATE DATABASE production_manager;
```

Editar o `application.properties`:

```properties
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/production_manager
quarkus.datasource.username=SEU_USER
quarkus.datasource.password=SUA_SENHA
```

### 3. Rodar o back

```bash
./mvnw quarkus:dev
```

API sobe em:

```
http://localhost:8080
```

---

## 💻 Front-end (React + Vite)

### 1. Entrar na pasta

```bash
cd frontend
```

### 2. Instalar dependências

```bash
npm install
```

### 3. Rodar o front

```bash
npm run dev
```

Front disponível em:

```
http://localhost:5173
```

---

## 🔗 Comunicação front ↔ back

No front, verifique o arquivo de API (axios):

```ts
baseURL: "http://localhost:8080"
```

---

## ▶️ Ordem para rodar

1. Subir MySQL
2. Rodar back
3. Rodar front

---

## ✅ Pronto

A aplicação já deve funcionar com:

* CRUD de produtos
* CRUD de matérias-primas
* Composição de produtos
* Dashboard com métricas

