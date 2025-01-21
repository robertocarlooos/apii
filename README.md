# Pas_projeto
# **Gerenciador de Medicamentos e Notificações**

Um aplicativo Android para ajudar os usuários a gerenciar seus medicamentos, configurar lembretes para tomá-los e visualizar históricos.

## **Índice**

1. [Descrição](#descrição)
2. [Funcionalidades](#funcionalidades)
3. [Pré-requisitos](#pré-requisitos)
4. [Configuração](#configuração)
5. [Estrutura do Projeto](#estrutura-do-projeto)
6. [APIs](#apis)
7. [Contribuições](#contribuições)
8. [Licença](#licença)

---

## **Descrição**

Este aplicativo foi desenvolvido para auxiliar usuários no gerenciamento de medicamentos e lembretes personalizados. Inclui funcionalidades para adicionar, editar e excluir medicamentos e notificações, bem como visualizar o histórico de uso.

---

## **Funcionalidades**

- Gerenciamento de medicamentos: adição, edição e exclusão.
- Configuração de lembretes para tomar medicamentos.
- Notificações programadas para lembrar o usuário de tomar o medicamento.
- Visualização do histórico de medicamentos tomados.
- Design intuitivo e responsivo.

---

## **Pré-requisitos**

Certifique-se de ter as seguintes ferramentas instaladas em seu ambiente:

- [Android Studio](https://developer.android.com/studio) (versão mais recente)
- Java JDK 8 ou superior
- Emulador ou dispositivo físico Android (versão mínima do Android suportada pelo app)

---
## **Estrutura do Projeto**

Abaixo está a descrição da estrutura principal do projeto:

### **1. Diretório principal: `app/src/main/java/com/example/medimate_java/`**

Este diretório contém o código-fonte principal do aplicativo. Aqui estão os arquivos e classes mais importantes:

- **`MainActivity.java`**  
  Ponto de entrada do aplicativo, responsável por carregar os fragmentos e gerenciar a navegação principal.

- **Fragments**:
  - **`AdicionarRecordatorio.java`**  
    Classe responsável pela configuração e salvamento de lembretes. Gerencia a interação do usuário ao definir horários e frequência de notificações.
  - **`Notificacoes.java`**  
    Exibe a lista de lembretes ativos configurados pelo usuário.
  - **`Historico.java`**  
    Tela para exibição do histórico de medicamentos tomados pelo usuário. Permite deletar registros do histórico.
  - **`Fragment_home_screen.java`**  
    Tela principal que exibe medicamentos disponíveis, com opções de pesquisa, edição e exclusão.

- **Adapters**:
  - **`MedicamentoAdapter.java`**  
    Classe responsável por adaptar a lista de medicamentos para exibição em um RecyclerView.

- **Modelos**:
  - **`Medicamento.java`**  
    Representa a entidade de um medicamento. Contém propriedades como nome, dose, tipo, quantidade e categoria.
  - **`Recordatorio.java`**  
    Representa lembretes configurados pelo usuário. Contém informações sobre o medicamento relacionado, horário, frequência e status ativo.
  - **`HistorialToma.java`**  
    Representa um registro no histórico de medicamentos tomados.

- **Notificações**:
  - **`NotificationReceiver.java`**  
    Classe que recebe os alarmes configurados e exibe notificações para o usuário.

- **API**:
  - **`APIService.java`**  
    Interface contendo todas as definições de chamadas para a API REST, como obter, adicionar ou excluir medicamentos e lembretes.
  - **`RetrofitClient.java`**  
    Configuração da instância do Retrofit para comunicação com o backend.

---

### **2. Diretório de Layouts: `app/src/main/res/layout/`**

Contém os arquivos XML que definem o design das telas do aplicativo.

- **Telas principais**:
  - **`fragment_adicionar_recordatorio.xml`**  
    Tela para configuração de lembretes.
  - **`fragment_notificacoes.xml`**  
    Tela para exibição dos lembretes ativos.
  - **`fragment_historico.xml`**  
    Tela para exibição do histórico de medicamentos tomados.
  - **`fragment_home_screen.xml`**  
    Tela inicial do aplicativo com lista de medicamentos e barra de pesquisa.

- **Itens de layout**:
  - **`medicamento_item.xml`**  
    Design de cada item da lista de medicamentos.
  - **`notificacao_item.xml`**  
    Design de cada item da lista de lembretes.
  - **`historico_item.xml`**  
    Design de cada item na tela de histórico.

---
