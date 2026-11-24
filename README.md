# CRUD

Aplicação desktop em **Java** com interface construída em **JavaFX puro** (sem FXML/Scene Builder) e persistência de dados no **SQL Server** via **JDBC**, seguindo uma organização em camadas (`Model`, `Controller`, `DAO` e `View`).

---

## Funcionalidades

- **Criar (`Create`):** cadastra novos livros com validação de campos obrigatórios, número de páginas positivo e prevenção de `ID` duplicado.
- **Listar (`Read`):** carrega os registros do SQL Server e exibe em uma `TableView` reativa (`ObservableList`).
- **Atualizar (`Update`):** seleciona ou busca um livro pelo `ID` para editar título, número de páginas e gênero no banco.
- **Remover (`Delete`):** exclui um livro selecionado na tabela ou informado pelo `ID`.
- **Sair:** encerra a aplicação JavaFX de forma limpa.

---

## Arquitetura do Projeto

```text
src/
├── Model/
│   └── Livro.java              # Entidade com atributos, construtor, getters/setters e toString()
├── DAO/
│   ├── ConexaoSQLServer.java   # Gerenciamento da conexão JDBC com o SQL Server
│   └── LivroDAO.java           # Operações SQL (INSERT, SELECT, UPDATE, DELETE) com PreparedStatement
├── Controller/
│   └── LivroController.java    # Regras de negócio, validações e sincronização com ObservableList
└── View/
    └── CrudView.java           # Interface gráfica em JavaFX puro (GridPane, TableView e eventos)
```

---

## Como Funciona o Fluxo

1. **Inicialização (`CrudView` → `LivroController` → `LivroDAO`):** ao abrir a janela, `LivroController` consulta `LivroDAO.listarTodos()` e preenche a `ObservableList<Livro>` exibida na `TableView`.
2. **Ações do usuário (`CrudView`):** o usuário preenche os campos ou clica em uma linha da tabela e aciona **Criar**, **Atualizar**, **Remover**, **Limpar** ou **Sair**.
3. **Validação e persistência (`LivroController` → `LivroDAO` → SQL Server):** `LivroController` valida os dados de entrada e delega a execução do SQL parametrizado ao `LivroDAO`, mantendo a interface desacoplada do banco.

---

## Configuração do Banco de Dados (SQL Server)

Execute o script abaixo no SQL Server Management Studio (SSMS) ou `sqlcmd`:

```sql
CREATE DATABASE BibliotecaDB;
GO

USE BibliotecaDB;
GO

CREATE TABLE Livros (
    id BIGINT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    n_paginas INT NOT NULL CHECK (n_paginas > 0),
    genero VARCHAR(100) NOT NULL
);
GO
```

Atualize as credenciais e a URL de conexão em `DAO/ConexaoSQLServer.java` conforme o seu ambiente (porta `1433`, instância nomeada, usuário/senha ou autenticação integrada).

---

## Pré-requisitos e Execução

- **JDK** compatível com o projeto e módulos **JavaFX** configurados.
- **Microsoft JDBC Driver for SQL Server (`mssql-jdbc`)** adicionado ao classpath ou gerenciador de build (Gradle/Maven).
- Instância do **SQL Server** ativa e acessível na rede/máquina local.

1. Configure o driver JDBC e os módulos JavaFX no seu ambiente/IDE.
2. Crie o banco `BibliotecaDB` e a tabela `Livros`.
3. Execute a classe `View.CrudView`.
