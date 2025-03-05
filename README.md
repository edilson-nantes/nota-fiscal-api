# Teste técnico IDS Software
Resolução do desafio fullstack do processo seletivo da IDS. Neste repositório está contido apenas o backend, para entrar no frondend acesse: https://github.com/edilson-nantes/nota-fiscal-frontend/

<h1 align="center">
<br>
Desafio processo Seletivo IDS
</h1>

</h1>

## Tecnologias

As tecnologias utilizadas foram:

- **Java** — Linguagem de programação usada para desenvolver a aplicação.

- **Quarkus** —  Framework Java utilizado para criar aplicações de microsserviços e APIs.

- **Panache** — Biblioteca de persistência de dados que fornece uma API mais simples e intuitiva para trabalhar com Hibernate.

- **PostgreSQL** — Banco de dados relacional utilizado para armazenar os dados do projeto.

 ## Descrição
 O projeto se baseia em construir uma aplicação para a entrada de notas fiscais. Onde o backend consiste em uma API Rest com operações CRUD para produtos, fornecedores e notas fiscais.

 ## Requisitos

 Para uma documentação completa da API, após rodar o projeto (descrito nesse README) acesse o link: http://localhost:8080/q/swagger-ui/

**Product - {GET} /products:** 
- Retorna todos os produtos cadastrados

**Product - {POST} /products:** 
- Cria um novo produto com os campos: code, description, situation, hasMovement
- O campo situation tem apenas os valores: Ativo e Inativo
- O campo situation é opicional e a API salva automaticamente como Ativo.
- O campo hasMovement é um boolean preenchido automaticamente pela API.

**Product - {PUT} /products/{id}:** 
- Atualiza um produto.
- Produtos com o campo hasMovement igual a true podem ter apenas o campo situation alterado.

**Product - {DELETE} /products/{id}:** 
- Deleta um produto.
- Produtos com o campo hasMovement igual a true não podem ser deletados.

**Supplier - {GET} /supliers:** 
- Retorna todos os fornecedores cadastrados

**Supplier - {POST} /supliers:** 
- Cria um novo fornecedor com os campos: code, legalName, email, phone, cnpj, situation, dataBaixa, hasMovement
- O campo situation tem apenas os valores: Ativo, Baixado, Suspenso.
- O campo situation é opicional e a API salva automaticamente como Ativo.
- O campo dataBaixa é opicional.
- O campo hasMovement é um boolean preenchido automaticamente pela API.

**Supplier - {PUT} /supliers/{id}:** 
- Atualiza um fornecedor.
- Fornecedores com o campo hasMovement igual a true podem ter apenas o campo situation alterado.

**Supplier - {DELETE} /supliers/{id}:** 
- Deleta um fornecedor.
- Fornecedores com o campo hasMovement igual a true não podem ser deletados.

**Nota Fiscal - {GET} /notas-fiscais:** 
- Retorna todas as notas fiscais cadastradas

**Nota Fiscal - {POST} /notas-fiscais:** 
- Cria uma nova nota fiscal com os campos: numberNota, suplier, emissionDate, address, totalValue.
- O campo totalValue é calculado automaticamente pela API, baseado nos itens presentes na nota fiscal.
- Ao cadastrar uma nova nota, o campo hasMovement do suplier é atualizado automaticamente para true.

**Nota Fiscal - {PUT} /notas-fiscais/{id}:** 
- Atualiza uma nota fiscal.

**Nota Fiscal - {DELETE} /notas-fiscais/{id}:** 
- Deleta uma nota fiscal.

**Item de Nota Fiscal - {GET} /items-nfiscal:** 
- Retorna todos os items de nota fiscal cadastrados

**Item de Nota Fiscal - {POST} /items-nfiscal:** 
- Cria um novo item nota fiscal com os campos: notaFiscal, product, quantity, unitValue, totalItemValue.
- O campo totalItemValue é calculado automaticamente pela API.
- Ao cadastrar um novo item, o campo hasMovement do product é atualizado automaticamente para true.

**Item de Nota Fiscal - {PUT} /items-nfiscal/{id}:** 
- Atualiza um item de nota fiscal.

**Item de Nota Fiscal - {DELETE} /items-nfiscal/{id}:** 
- Deleta um item de nota fiscal.


##  Download e Teste



-  Instalar o [Git](https://git-scm.com/), [Java](https://www.oracle.com/br/java/technologies/javase/jdk11-archive-downloads.html) + [Quarkus](https://quarkus.io/) + [Maven](https://maven.apache.org/download.cgi), [Docker](https://www.docker.com/) e [Imagem docker do PostgreSQL](https://hub.docker.com/_/postgres):

```bash
# Versões utilizadas no desenvolvimento.
 java --version
v22.14.0

 mvn --version
Apache Maven 3.8.7

 quarkus
3.2.12

docker -v
Docker version 27.5.1, build a187fa5
```

```bash
# Clonar o repositório
 git clone https://github.com/edilson-nantes/nota-fiscal-api/

#Instalar o docker
 sudo apt-get install ./docker-desktop-amd64.deb

#Baixar a imagem do postgres
 docker pull postgres

#Iniciar o container postgres(no desenvolvimento foram usadas as mesmas credenciais padrão da imagem)
 docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres

#Entrar no diretório
 cd nota-fiscal-api

#Edite o arquivo application.properties para refletir as configurações corretas do banco de dados
```
- As credenciais do arquivo application.properties podem ser alteradas de acordo com a necessidade, mas as credenciais usadas no desenvolvimento foram:

```bash
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=mysecretpassword
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/nota_fiscal
quarkus.hibernate-orm.database.generation=update
```

- Digite o seguinte comando para rodar a aplicação:
```bash
mvn quarkus:dev
```

- Para rodar os testes unitários com relatório de cobertura digite:
```bash
mvn test jacoco:report -Djacoco.agent
```
- Em seguida acesse a pasta target/site/jacoco, gerada na raíz do projeto, e abra o arquivo html no seu navegador

- Por ultimo abra sua ferramenta de teste para API e digite o seguinte base URL:

```
localhost:8080
```

---
