
# Desafio Serasa

API RESTful, para cadastro de pessoas com score e suas regiões de afinidades

- Java 11
- Maven 
- Spring Boot
- Swagger
- H2
- Caching
- Circuit Breaker
- Lombok
- Spring Security


## Documentação da API

#### Swagger

```http
  http://localhost:8080/desafio-serasa/swagger-ui.html
```

## Rodando localmente

Clone o projeto

```bash
  git clone https://github.com/romaneli13/DesafioScoreJava.git
```

Entre no diretório do projeto

```bash
  cd my-project
```

Instale as dependências

```bash
  mvn clean install
```

Inicie o projeto

```bash
  mvn spring-boot:run
```

## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente no seu .env

`DB_USERNAME`

`DB_PASSWORD`


## Rodando os testes

Para rodar os testes, rode o seguinte comando

```bash
  mvn test
```


Testes executados

```bash
[INFO] Results:
[INFO]
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
```

## Referência

 - [Caching Spring Boot](https://spring.io/guides/gs/caching/)
 - [Circuit Breaker Resilience4j](https://resilience4j.readme.io/docs/circuitbreaker)
 - [Lombok](https://projectlombok.org/features/)
 - [Spring Security](https://spring.io/projects/spring-security)

