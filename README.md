# Desafio Técnico – Simulação de Crédito

## Contexto

Este projeto tem como objetivo disponibilizar uma API de simulação de empréstimo ,permitindo que qualquer pessoa ou sistema consulte as condições oferecidas para uma negociação de crédito.

---

## Tecnologias e Ferramentas Utilizadas

- **Java 17+**
- **QUARKUS** (estrutura simples e de fácil compreensão)
- **SQL Server** (escolha alinhada com soluções utilizadas pelo banco Caixa)
- **Docker & Docker Compose** (para execução em container)
- **Swagger** (para documentação das rotas e testes da API)
- **EventHub** (simulação de integração com áreas de relacionamento)

---

## Observações Técnicas

- Exceções foram tratadas adequadamente para melhor confiabilidade.
- O Swagger foi incluído por ser amplamente utilizado e facilitar testes e documentação.
- As chaves e secrets foram armazenadas no código apenas para facilitar a avaliação; em produção, isso deve ser feito de forma segura.
- A escolha do SQL Server localmente e da arquitetura simples foi baseada na facilidade de compreensão e na homologação pelo banco Caixa.
- A arquitetura do projeto é propositalmente simples para facilitar entendimento e manutenção.

---

## Pré-requisitos

- Java 17+
- Maven 3+
- Docker
- Docker Compose

---

## Passo a Passo para Build e Execução

### 1. Build do projeto com Maven

No diretório raiz do projeto, execute:

```bash
./mvnw package -DskipTests
```

Isso irá compilar o projeto, executar os testes e gerar o jar.

### 2. Executando via Docker Compose

No mesmo diretório, execute:

```bash
docker-compose up --build
```

Isso irá:

Criar os containers necessários (API + banco SQL Server).

Subir a aplicação da API pronta para receber requisições.

A API estará disponível em http://localhost:8080.

### 3 Acessando a aplicação

Foi incluido um documento Swagger para testar todas as rotas da aplicação 

```bash
http://localhost:8080/q/swagger-ui/
```
