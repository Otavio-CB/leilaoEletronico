# Sistema de Leilões - API

**Descrição**  
Este projeto implementa uma API para gerenciamento de leilões, dispositivos, veículos, clientes e instituições financeiras. Utiliza o framework **Micronaut** para criação de APIs robustas e performáticas.

## 📋 Requisitos

Antes de iniciar, certifique-se de que o ambiente atende aos seguintes requisitos:

- **Java**: 17
- **Micronaut**: última versão compatível
- **Gradle**: 7.0+
- **Banco de Dados**: MySQL
- **Docker** e **Docker Compose** (opcional, para execução em contêineres)
- **Yarn**: para gerenciamento de dependências front-end (caso necessário)

---

## 📂 Estrutura do Projeto

Os principais componentes do sistema estão organizados da seguinte forma:

### **Controllers**
Responsáveis por expor os endpoints da API:
- **ClienteController**: `/clientes`
- **DispositivoController**: `/dispositivos`
- **InstituicaoFinanceiraController**: `/instituicoes`
- **LanceController**: `/lances`
- **LeilaoController**: `/leiloes`
- **VeiculoController**: `/veiculos`

---

### ⬇️ Seção de Downloads

Os arquivos com extensão `.det` são baixados automaticamente pela aplicação e armazenados na pasta de destino padrão, que é:

```plaintext
/resources/det
```

---

### 📂  Camadas Principais

- **Controller**:  
  Expõe os endpoints para interação com os usuários ou sistemas externos. É responsável por receber as requisições, processar os dados básicos e chamar as camadas de serviço.

- **Service**:  
  Implementa as regras de negócio e lógica da aplicação. Trata os dados recebidos dos controllers e delega operações de persistência para a camada de repositório.

- **Repository**:  
  Realiza as operações de persistência no banco de dados. Fornece uma interface para interagir com os dados de maneira eficiente e segura.

- **Models**:  
  Contém as entidades representadas no sistema. Essas classes refletem as tabelas do banco de dados ou objetos essenciais do domínio.

- **DTOs**:  
  Objetos para transferência de dados entre as camadas da aplicação, evitando expor diretamente as entidades.

- **Enums**:  
  Define valores constantes e categóricos usados no sistema, como estados, tipos e categorias, promovendo consistência.

- **Mapper**:  
  Responsável por converter objetos entre diferentes camadas (por exemplo, de Model para DTO e vice-versa). Pode usar bibliotecas como MapStruct ou modelMapper.

- **Utils**:  
  Contém classes utilitárias e funções de apoio reutilizáveis no projeto, como manipuladores de strings, geradores de IDs, validações genéricas etc.

- **Exceptions**:  
  Define as exceções personalizadas do sistema. Essa camada organiza erros específicos que podem ocorrer no domínio da aplicação.

- **Handlers**:  
  Contém classes responsáveis por capturar e processar exceções que ocorrem em toda a aplicação.

--- 

## ⚙️ Configuração do Ambiente Local

### **1. Clonar o Repositório**

```bash
git clone https://github.com/Otavio-CB/leilaoEletronico.git
cd leilaoEletronico
```

### **2. Instalar Dependências**

```bash
./gradlew build
```

### **3. Configurar o Banco de Dados**

Certifique-se de configurar as credenciais do banco de dados no arquivo `application.yml`:

```yaml
datasources:
  default:
    url: jdbc:mysql://localhost:3306/leilao_eletronico
    username: root
    password: admin123
```

### **4. Executar o Projeto**

```bash
./gradlew run
```

A API estará disponível em: [http://localhost:8080](http://localhost:8080)

---

## 🐳 Execução com Docker

### **1. Pré-requisitos**
- [Instalar o Docker](https://docs.docker.com/get-docker/)
- [Instalar o Docker Compose](https://docs.docker.com/compose/install/)

### **2. Subir os Contêineres**
O arquivo `docker-compose.yml` já está configurado. Execute:

```bash
docker-compose up --build -d
```

**Importante:** O comando acima inicializa apenas o banco de dados. Para rodar a aplicação, execute o seguinte comando em um terminal separado na pasta da aplicação:

```bash
./gradlew run
```

### **3. Parar os Contêineres**
```bash
docker-compose down
```

### **4. Recriar Contêineres e Dados**
```bash
docker-compose down --volumes
```

## 📖 Documentação dos Endpoints

[![Endpoints](https://img.shields.io/badge/📖_Documentação-Wiki-blue?style=for-the-badge)](https://github.com/Otavio-CB/leilaoEletronico/wiki/Home-%F0%9F%8F%A0)
[![Swagger](https://img.shields.io/badge/📖_Swagger-Wiki-blue?style=for-the-badge)](http://localhost:8080/swagger/views/swagger-ui/)
