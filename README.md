# Gerenciador de Medicamentos e Notificações

Um aplicativo Android para ajudar os usuários a gerenciar seus medicamentos, configurar lembretes para tomá-los e visualizar históricos.

## Índice
- [Descrição](#descrição)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [APIs](#apis)
- [Contribuições](#contribuições)
- [Licença](#licença)

## Descrição

Este aplicativo foi desenvolvido para auxiliar usuários no gerenciamento de medicamentos e lembretes personalizados. Inclui funcionalidades para adicionar, editar e excluir medicamentos e notificações, bem como visualizar o histórico de uso.

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

## Licença

Este projeto está licenciado sob a Licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.
