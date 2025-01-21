# Gerenciador de Medicamentos e Notificações

Um aplicativo Android para ajudar os usuários a gerenciar seus medicamentos, configurar lembretes para tomá-los e visualizar históricos.

## Índice
- [Descrição](#descrição)
- [Estrutura de base de dados](#estrutura-de-base-de-dados)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [APIs](#apis)
- [Instalação e Configuração](#instalação-e-configuração)
- [Contribuições](#contribuições)
- [Licença](#licença)

## Descrição

Este aplicativo foi desenvolvido para auxiliar usuários no gerenciamento de medicamentos e lembretes personalizados. Inclui funcionalidades para adicionar, editar e excluir medicamentos e notificações, bem como visualizar o histórico de uso.

A parte web do projeto possui as mesmas funcionalidades que a versão móvel, permitindo o gerenciamento de medicamentos, configuração de lembretes e visualização do histórico de forma integrada.

## Estrutura de base de dados
![Captura de ecrã 2025-01-21 235257](https://github.com/user-attachments/assets/c1ce9bcb-6093-4026-b0ce-9677bc609203)


## Funcionalidades

- Gerenciamento de medicamentos: adição, edição e exclusão.
- Configuração de lembretes para tomar medicamentos.
- Notificações programadas para lembrar o usuário de tomar o medicamento.
- Visualização do histórico de medicamentos tomados.
- Design intuitivo e responsivo.

## Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em seu ambiente:

- Android Studio (versão mais recente)
- Java JDK 8 ou superior
- Emulador ou dispositivo físico Android (versão mínima do Android suportada pelo app)

## Configuração

1. Clone este repositório:
   ```bash
   git clone https://github.com/seu_usuario/seu_repositorio.git
   ```

2. Abra o projeto no Android Studio:
   - Selecione "Open an Existing Project" no Android Studio e escolha a pasta clonada.

3. Configure as dependências:
   - Verifique o arquivo `build.gradle` e sincronize o projeto.

4. Execute o aplicativo:
   - Conecte seu dispositivo Android ou inicie um emulador.
   - Clique em "Run" no Android Studio.

## Estrutura do Projeto

Abaixo está a descrição da estrutura principal do projeto:

### Diretório principal: `app/src/main/java/com/example/medimate_java/`

Este diretório contém o código-fonte principal do aplicativo. Aqui estão os arquivos e classes mais importantes:

- **MainActivity.java**: Ponto de entrada do aplicativo, responsável por carregar os fragmentos e gerenciar a navegação principal.

#### Fragments:
- **AdicionarRecordatorio.java**: Classe responsável pela configuração e salvamento de lembretes. Gerencia a interação do usuário ao definir horários e frequência de notificações.
- **Notificacoes.java**: Exibe a lista de lembretes ativos configurados pelo usuário.
- **Historico.java**: Tela para exibição do histórico de medicamentos tomados pelo usuário. Permite deletar registros do histórico.
- **Fragment_home_screen.java**: Tela principal que exibe medicamentos disponíveis, com opções de pesquisa, edição e exclusão.

#### Adapters:
- **MedicamentoAdapter.java**: Classe responsável por adaptar a lista de medicamentos para exibição em um RecyclerView.

#### Modelos:
- **Medicamento.java**: Representa a entidade de um medicamento. Contém propriedades como nome, dose, tipo, quantidade e categoria.
- **Recordatorio.java**: Representa lembretes configurados pelo usuário. Contém informações sobre o medicamento relacionado, horário, frequência e status ativo.
- **HistorialToma.java**: Representa um registro no histórico de medicamentos tomados.

#### Notificações:
- **NotificationReceiver.java**: Classe que recebe os alarmes configurados e exibe notificações para o usuário.

#### API:
- **APIService.java**: Interface contendo todas as definições de chamadas para a API REST, como obter, adicionar ou excluir medicamentos e lembretes.
- **RetrofitClient.java**: Configuração da instância do Retrofit para comunicação com o backend.

### Diretório de Layouts: `app/src/main/res/layout/`

Contém os arquivos XML que definem o design das telas do aplicativo.

#### Telas principais:
- **fragment_adicionar_recordatorio.xml**: Tela para configuração de lembretes.
- **fragment_notificacoes.xml**: Tela para exibição dos lembretes ativos.
- **fragment_historico.xml**: Tela para exibição do histórico de medicamentos tomados.
- **fragment_home_screen.xml**: Tela inicial do aplicativo com lista de medicamentos e barra de pesquisa.
- **fragment_adicionar_medicamento.xml**: Tela para adicionar um novo medicamento.
- **fragment_editar_medicamento.xml**: Tela para editar os detalhes de um medicamento.
- **fragment_editar_recordatorio.xml**: Tela para edição de lembretes existentes.
- **fragment_main_screen.xml**: Tela principal do aplicativo com navegação geral.
- **fragment_recuardatorios.xml**: Tela para visualização e gerenciamento de lembretes configurados.

#### Itens de layout:
- **medicamento_item.xml**: Design de cada item da lista de medicamentos.
- **notificacao_item.xml**: Design de cada item da lista de lembretes.
- **historico_item.xml**: Design de cada item na tela de histórico.

## APIs

Este projeto utiliza APIs para sincronização de dados entre o aplicativo e o servidor. As principais chamadas incluem:

- **Obter medicamentos**:
  ```http
  GET /api/medicamentos
  ```
  Retorna a lista de medicamentos associados ao usuário.

- **Adicionar medicamento**:
  ```http
  POST /api/medicamentos
  ```
  Adiciona um novo medicamento ao usuário.

- **Editar medicamento**:
  ```http
  PUT /api/medicamentos/{id}
  ```
  Atualiza as informações de um medicamento existente.

- **Excluir medicamento**:
  ```http
  DELETE /api/medicamentos/{id}
  ```
  Remove um medicamento do usuário.

- **Obter lembretes**:
  ```http
  GET /api/recordatorios
  ```
  Retorna a lista de lembretes configurados.

- **Adicionar lembrete**:
  ```http
  POST /api/recordatorios
  ```
  Adiciona um novo lembrete ao usuário.

- **Editar lembrete**:
  ```http
  PUT /api/recordatorios/{id}
  ```
  Atualiza os detalhes de um lembrete configurado.

- **Excluir lembrete**:
  ```http
  DELETE /api/recordatorios/{id}
  ```
  Remove um lembrete existente.

- **Obter usuários**:
  ```http
  GET /api/usuarios
  ```
  Retorna a lista de usuários cadastrados no sistema.

- **Adicionar usuário**:
  ```http
  POST /api/usuarios
  ```
  Adiciona um novo usuário ao sistema.

- **Obter histórico de tomas**:
  ```http
  GET /api/historico
  ```
  Retorna a lista de registros de medicamentos tomados pelo usuário.

- **Adicionar histórico de tomas**:
  ```http
  POST /api/historico
  ```
  Adiciona um novo registro ao histórico de tomas do usuário.

- **Editar histórico de tomas**:
  ```http
  PUT /api/historico/{id}
  ```
  Atualiza as informações de um registro no histórico de tomas.

- **Excluir histórico de tomas**:
  ```http
  DELETE /api/historico/{id}
  ```
  Remove um registro do histórico de tomas do usuário.

## Instalação e Configuração

Para utilizar completamente o projeto, siga os passos abaixo:

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu_usuario/seu_repositorio.git
   ```

2. Instale o XAMPP:
   - Baixe e instale o [XAMPP](https://www.apachefriends.org/index.html).

3. Configure o banco de dados:
   - Crie o banco de dados chamado `Medimate` na página do PHPMyAdmin do XAMPP.

4. Configure o backend:
   - Navegue até o diretório `website/medimate` dentro da pasta clonada.
   - Execute o seguinte comando para migrar o banco de dados:
     ```bash
     php artisan migrate
     ```
   - Após migrar o banco de dados, importe os dados do arquivo `medimate.sql`.

5. Inicie o backend:
   - Execute o comando:
     ```bash
     php artisan serve
     ```

6. Instale e configure o Android Studio:
   - Abra o projeto no Android Studio e inicie o emulador.

7. Aproveite o aplicativo!

## Contribuições

Contribuições são bem-vindas! Siga os passos abaixo para colaborar:

1. Faça um fork do repositório.
2. Crie um branch para sua funcionalidade/correção:
   ```bash
   git checkout -b minha-funcionalidade
   ```
3. Commit suas alterações:
   ```bash
   git commit -m "Adiciona minha funcionalidade"
   ```
4. Faça o push para o branch:
   ```bash
   git push origin minha-funcionalidade
   ```
5. Abra um Pull Request no repositório original.

## Autores

- Roberto López
- Manuel Machado
  
  


