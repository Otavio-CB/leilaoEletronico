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

### **Camadas Principais**

- **Controller**: Expõe os endpoints para interação com os usuários ou sistemas externos.
- **Service**: Implementa as regras de negócio e lógica da aplicação.
- **Repository**: Realiza as operações de persistência no banco de dados.
- **Models**: Contém as entidades representadas no sistema.
- **DTOs**: Objetos para transferência de dados entre as camadas da aplicação.
- **Enums**: Define valores constantes e categóricos usados no sistema.
- **Mapper**: Responsável por converter objetos entre diferentes camadas (ex.: de Model para DTO e vice-versa).
- **Utils**: Contém classes utilitárias e funções de apoio reutilizáveis no projeto.

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
docker-compose up --build
```

### **3. Parar os Contêineres**
```bash
docker-compose down
```

### **4. Recriar Contêineres e Dados**
```bash
docker-compose down --volumes
```

---

## 📖 Documentação dos Endpoints

[![Endpoints](https://img.shields.io/badge/📖_Documentação-Wiki-blue?style=for-the-badge)](https://github.com/usuario/repositorio/wiki/Endpoints)
