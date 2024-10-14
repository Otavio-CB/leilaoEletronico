# Sistema de Leilões - API

Este projeto implementa uma API de gerenciamento de leilões, dispositivos, veículos, clientes e instituições financeiras utilizando **Micronaut**. A seguir estão os procedimentos para compilar, executar e testar o projeto, além de exemplos de uso dos endpoints disponíveis.

## Requisitos

Antes de começar, certifique-se de que o ambiente de desenvolvimento atenda aos seguintes requisitos:

- Java 17
- Micronaut
- Yarn (para gerenciamento de dependências)
- H2 (banco de dados utilizado no projeto)

## Estrutura do Projeto

Os principais controllers deste projeto estão localizados em:

- **ClienteController**: `/clientes`
- **DispositivoController**: `/dispositivos`
- **InstituicaoFinanceiraController**: `/instituicoes`
- **LeilaoController**: `/leiloes`
- **VeiculoController**: `/veiculos`

Esses controllers fornecem operações CRUD básicas, com validações e associações entre entidades.

## Configuração do Projeto

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   cd <nome-do-projeto>
   ```

2. Instale as dependências:
   ```bash
   ./gradlew build
   ```

3. Execute o projeto:
   ```bash
   ./gradlew run
   ```

4. O aplicativo estará disponível em: `http://localhost:8080`.

## Endpoints Disponíveis

### Clientes

- **Criar Cliente**: `POST /clientes`
    - Envia um objeto `ClienteDTO` no corpo da requisição para criar um cliente.

- **Buscar Cliente por ID**: `GET /clientes/{id}`
    - Retorna os detalhes de um cliente específico pelo `id`.

- **Atualizar Cliente**: `PUT /clientes/{id}`
    - Atualiza um cliente com o `id` fornecido.

- **Remover Cliente**: `DELETE /clientes/{id}`
    - Remove um cliente pelo `id`.

### Dispositivos

- **Criar Dispositivo**: `POST /dispositivos/{leilaoId}`
    - Associa um dispositivo a um leilão existente.

- **Buscar Dispositivo por ID**: `GET /dispositivos/{id}`
    - Retorna os detalhes de um dispositivo específico.

- **Atualizar Dispositivo**: `PUT /dispositivos/{id}`
    - Atualiza as informações de um dispositivo.

- **Remover Dispositivo**: `DELETE /dispositivos/{id}`
    - Remove um dispositivo pelo `id`.

- **Reassociar Dispositivo a Outro Leilão**: `PUT /dispositivos/{id}/reassociar/{novoLeilaoId}`
    - Reassocia um dispositivo a um novo leilão.

### Instituições Financeiras

- **Criar Instituição Financeira**: `POST /instituicoes`
    - Cria uma nova instituição financeira.

- **Buscar Instituição por ID**: `GET /instituicoes/{id}`
    - Retorna os detalhes de uma instituição financeira.

- **Atualizar Instituição**: `PUT /instituicoes/{id}`
    - Atualiza os dados de uma instituição financeira.

- **Remover Instituição**: `DELETE /instituicoes/{id}`
    - Remove uma instituição pelo `id`.

### Leilões

- **Criar Leilão**: `POST /leiloes`
    - Cria um novo leilão e associa instituições financeiras.

- **Buscar Leilão por ID**: `GET /leiloes/{id}`
    - Retorna os detalhes de um leilão.

- **Atualizar Leilão**: `PUT /leiloes/{id}`
    - Atualiza as informações de um leilão.

- **Remover Leilão**: `DELETE /leiloes/{id}`
    - Remove um leilão pelo `id`.

### Veículos

- **Criar Veículo**: `POST /veiculos/{leilaoId}`
    - Associa um veículo a um leilão existente.

- **Buscar Veículo por ID**: `GET /veiculos/{id}`
    - Retorna os detalhes de um veículo.

- **Atualizar Veículo**: `PUT /veiculos/{id}`
    - Atualiza as informações de um veículo.

- **Remover Veículo**: `DELETE /veiculos/{id}`
    - Remove um veículo pelo `id`.

- **Reassociar Veículo a Outro Leilão**: `PUT /veiculos/{id}/reassociar/{novoLeilaoId}`
    - Reassocia um veículo a um novo leilão.


### Lances

- **Criar Lance**: `POST /lances`
    - Envia um objeto `LanceDTO` no corpo da requisição para registrar um novo lance.

- **Buscar Todos os Lances**: `GET /lances`
    - Retorna todos os lances registrados.

- **Atualizar Lance**: `PUT /lances/{id}`
    - Atualiza um lance com o `id` fornecido.

- **Remover Lance**: `DELETE /lances/{id}`
    - Remove um lance pelo `id`.

## Executar o Projeto com Docker

### Pré-requisitos

Certifique-se de ter o Docker e Docker Compose instalados no seu ambiente.

1. [Instalar o Docker](https://docs.docker.com/get-docker/)
2. [Instalar o Docker Compose](https://docs.docker.com/compose/install/)

### Instruções para rodar o projeto com Docker

1. **Configurar o arquivo `docker-compose.yml`**:

   O arquivo `docker-compose.yml` já está configurado para criar os contêineres da aplicação e do MySQL. Basta rodar o seguinte comando para subir os serviços:

   ```bash
   docker-compose up --build
   ```

3. **Verificar os contêineres**:

   Após rodar o comando acima, a aplicação estará disponível em: `http://localhost:8080`. Para verificar se os contêineres estão em execução, utilize:

   ```bash
   docker ps
   ```

### Parar os contêineres

Para parar os serviços, basta rodar:

```bash
docker-compose down
```

### Recriar contêineres e bancos de dados

Se você precisar recriar o banco de dados ou os contêineres, use o seguinte comando:

```bash
docker-compose down --volumes
```

Com isso, o banco de dados será recriado do zero na próxima execução.

---

Esta seção orienta como utilizar Docker para rodar o projeto com um banco de dados MySQL dentro de contêineres, facilitando o ambiente de desenvolvimento e execução.

## Testes

### Executar testes unitários

Para garantir que a aplicação esteja funcionando conforme o esperado, você pode executar os testes unitários incluídos no projeto:

```bash
./gradlew test
```

Os resultados dos testes serão exibidos no terminal.

## Observações

- O projeto utiliza o banco de dados H2 como padrão, que pode ser acessado pela URL: `http://localhost:8080/h2-console`.
- O arquivo de configuração `application.properties` contém as configurações de banco de dados e outras variáveis de ambiente.
