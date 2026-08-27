# 🛸 Portal Rick and Morty

Aplicativo Android desenvolvido em **Kotlin + Jetpack Compose** para consulta e armazenamento local de personagens, localizações e episódios do universo de **Rick and Morty**.

O projeto consome a API pública do Rick and Morty, armazena os dados utilizando **Room** e foi desenvolvido para continuar funcionando mesmo quando o dispositivo estiver **sem conexão com a internet**.

---

## 🔐 Acesso ao aplicativo

> **O aplicativo exige login para acessar o conteúdo.**

Ao iniciar o aplicativo, o usuário é direcionado para a tela de login.
O acesso às telas de personagens, localizações, episódios e Meu Portal somente é liberado após a autenticação.

### Credenciais para teste

| Campo       | Valor      |
| ----------- | ---------- |
| **Usuário** | `admin`    |
| **Senha**   | `admin123` |

A credencial de administrador é cadastrada no banco **Room** na primeira execução do aplicativo e utilizada para validar o login.

### Fluxo de acesso

```text
┌──────────────┐
│    Login     │
└──────┬───────┘
       │
       │ admin / admin123
       ▼
┌──────────────────────────────┐
│      Portal Rick and Morty   │
├──────────────────────────────┤
│ Personagens                  │
│ Localizações                 │
│ Episódios                    │
│ Meu Portal                   │
└──────────────────────────────┘
```

Caso usuário ou senha estejam incorretos, uma mensagem de erro é apresentada na própria tela de login.

O botão de **sair/logout** retorna o usuário para a tela de login.

> **Importante:** este login possui finalidade didática. Não foi implementado hash de senha ou autenticação através de backend, conforme especificação do projeto.

---

## 📱 Funcionalidades

### 👽 Personagens

* Lista de personagens da API.
* Exibição de:

  * Nome
  * Espécie
  * Status
  * Imagem
* Tela de detalhes.
* Informações adicionais, como origem e gênero.
* Exibição dos episódios relacionados.

### 🌎 Localizações

* Lista de localizações.
* Exibição de:

  * Nome
  * Tipo
  * Dimensão
  * Quantidade de residentes
* Tela de detalhes.

### 📺 Episódios

* Lista de episódios.
* Exibição de:

  * Nome
  * Temporada/episódio
  * Data de estreia
* Tela de detalhes.
* Informações relacionadas ao elenco/personagens.

### 🌀 Meu Portal

Tela destinada às funcionalidades relacionadas ao dispositivo:

* Localização atual.
* Latitude e longitude.
* Última localização armazenada.
* Sincronização manual dos dados.
* Informação sobre a última sincronização.
* Funcionamento offline da última localização salva.
* Logout.

---

## 🌐 API

O aplicativo utiliza a API pública do **Rick and Morty**.

Endpoints utilizados:

```text
/api/character
/api/character/{id}

/api/location
/api/location/{id}

/api/episode
/api/episode/{id}
```

A comunicação com a API é realizada utilizando **Retrofit**, com modelos DTO específicos para cada entidade.

A API é utilizada para atualizar os dados locais. As telas não dependem diretamente da API para exibir as informações.

---

## 📴 Funcionamento Offline

Um dos principais objetivos do projeto é permitir que o aplicativo continue funcionando mesmo sem internet.

A arquitetura segue o fluxo:

```text
                 ┌───────────────┐
                 │    Retrofit   │
                 │      API      │
                 └───────┬───────┘
                         │
                         │ sincronização
                         ▼
┌──────────────┐   ┌───────────────┐
│     UI       │◄──│  Repository   │
└──────────────┘   └───────┬───────┘
                           │
                           ▼
                    ┌─────────────┐
                    │    Room     │
                    │  Banco local│
                    └─────────────┘
```

As telas consultam os dados armazenados no **Room** por meio de `Flow`.

A API é utilizada apenas para atualizar o banco local.

Dessa forma, o aplicativo consegue:

* Abrir sem internet.
* Exibir personagens previamente sincronizados.
* Exibir localizações previamente sincronizadas.
* Exibir episódios previamente sincronizados.
* Recuperar a última localização salva.
* Continuar navegando sem quebrar quando a conexão estiver indisponível.

Quando não há conexão, o aplicativo informa que está utilizando os dados salvos localmente.

---

## 🗄️ Banco de dados

O armazenamento local é feito utilizando **Room**.

Entidades principais:

```text
CharacterEntity
LocationEntity
EpisodeEntity
UserEntity
```

### CharacterEntity

```text
id
name
status
species
imageUrl
```

### LocationEntity

```text
id
name
type
dimension
residents
```

### EpisodeEntity

```text
id
name
airDate
episode
characters
```

### UserEntity

Utilizada para armazenar o usuário responsável pelo acesso ao aplicativo.

Na primeira execução, o banco realiza o cadastro do usuário administrador:

```text
Usuário: admin
Senha: admin123
```

---

## 🧭 Navegação

A navegação do aplicativo é construída utilizando **Navigation Compose**.

A tela de login não possui a barra de navegação principal.

Após o login, o usuário tem acesso às principais áreas:

```text
characters
locations
episodes
myPortal
```

Cada entidade possui também uma rota específica para sua tela de detalhes:

```text
character/{id}
location/{id}
episode/{id}
```

### Estrutura de navegação

```text
Login
  │
  └── Personagens
        │
        └── Detalhes do personagem

Personagens ─────┐
Localizações ────┼── NavigationBar
Episódios ───────┤
Meu Portal ──────┘
```

---

## 📍 Localização

A tela **Meu Portal** utiliza a localização do dispositivo.

É utilizado o `FusedLocationProviderClient` para obter:

* Latitude
* Longitude

O usuário pode solicitar sua localização através do botão:

> **Localizar meu portal**

A última localização obtida é armazenada localmente para que possa ser exibida posteriormente mesmo sem conexão com a internet.

Caso a permissão de localização seja recusada, o aplicativo apresenta uma mensagem explicativa.

---

## 🔔 Notificações

O aplicativo utiliza notificações para informar o término das sincronizações.

Após uma sincronização concluída, o usuário recebe uma notificação semelhante a:

```text
Portal sincronizado

Personagens, localizações e episódios
foram atualizados na Cidadela.
```

Ao tocar na notificação, o aplicativo pode direcionar o usuário para a área de **Personagens**.

Em versões recentes do Android, a permissão para envio de notificações é solicitada em tempo de execução.

---

## 🔄 Sincronização

O projeto possui dois mecanismos de sincronização.

### Sincronização manual

Disponível na tela **Meu Portal** através do botão:

> **Sincronizar agora**

Durante a operação, o aplicativo apresenta um estado de sincronização/progresso.

Ao finalizar:

1. Os dados são obtidos da API.
2. Os dados são armazenados no Room.
3. A sincronização é finalizada.
4. Uma notificação é disparada.

### Sincronização automática

A sincronização periódica é realizada através do **WorkManager**.

O `SyncWorker`:

* Executa a sincronização em segundo plano.
* Consulta a API.
* Atualiza o banco Room.
* Possui restrição para execução somente quando houver conexão de rede.
* Executa periodicamente.

---

## ⚙️ Foreground Service

A sincronização manual utiliza um **Foreground Service** para manter a operação ativa enquanto os dados são atualizados.

Durante a sincronização, uma notificação persistente informa que o processo está em execução.

Após a conclusão, o serviço é encerrado automaticamente.

---

## 🏗️ Arquitetura do projeto

O projeto está organizado separando responsabilidades entre **Data, Domain, UI e serviços do Android**.

```text
PortalRickAndMorty/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── br/
│           │       └── com/
│           │           └── curso/
│           │               └── portalrickandmorty/
│           │                   │
│           │                   ├── MainActivity.kt
│           │                   ├── PortalApplication.kt
│           │                   │
│           │                   ├── data/
│           │                   │   ├── local/
│           │                   │   │   ├── AppDatabase.kt
│           │                   │   │   ├── dao/
│           │                   │   │   │   ├── CharacterDao.kt
│           │                   │   │   │   ├── LocationDao.kt
│           │                   │   │   │   ├── EpisodeDao.kt
│           │                   │   │   │   └── UserDao.kt
│           │                   │   │   │
│           │                   │   │   └── entity/
│           │                   │   │       ├── CharacterEntity.kt
│           │                   │   │       ├── LocationEntity.kt
│           │                   │   │       ├── EpisodeEntity.kt
│           │                   │   │       └── UserEntity.kt
│           │                   │   │
│           │                   │   ├── remote/
│           │                   │   │   ├── RickAndMortyApi.kt
│           │                   │   │   ├── RetrofitInstance.kt
│           │                   │   │   └── dto/
│           │                   │   │       ├── CharacterDto.kt
│           │                   │   │       ├── LocationDto.kt
│           │                   │   │       └── EpisodeDto.kt
│           │                   │   │
│           │                   │   └── repository/
│           │                   │       ├── CharacterRepository.kt
│           │                   │       ├── LocationRepository.kt
│           │                   │       ├── EpisodeRepository.kt
│           │                   │       └── UserRepository.kt
│           │                   │
│           │                   ├── domain/
│           │                   │   └── model/
│           │                   │       ├── Character.kt
│           │                   │       ├── Location.kt
│           │                   │       └── Episode.kt
│           │                   │
│           │                   ├── ui/
│           │                   │   ├── navigation/
│           │                   │   │   ├── AppNavigation.kt
│           │                   │   │   └── Routes.kt
│           │                   │   │
│           │                   │   ├── login/
│           │                   │   │   ├── LoginScreen.kt
│           │                   │   │   └── LoginViewModel.kt
│           │                   │   │
│           │                   │   ├── characters/
│           │                   │   │   ├── CharactersScreen.kt
│           │                   │   │   ├── CharacterDetailScreen.kt
│           │                   │   │   └── CharacterViewModel.kt
│           │                   │   │
│           │                   │   ├── locations/
│           │                   │   │   ├── LocationsScreen.kt
│           │                   │   │   ├── LocationDetailScreen.kt
│           │                   │   │   └── LocationViewModel.kt
│           │                   │   │
│           │                   │   ├── episodes/
│           │                   │   │   ├── EpisodesScreen.kt
│           │                   │   │   ├── EpisodeDetailScreen.kt
│           │                   │   │   └── EpisodeViewModel.kt
│           │                   │   │
│           │                   │   └── portal/
│           │                   │       ├── MyPortalScreen.kt
│           │                   │       └── PortalViewModel.kt
│           │                   │
│           │                   ├── worker/
│           │                   │   └── SyncWorker.kt
│           │                   │
│           │                   ├── service/
│           │                   │   └── SyncForegroundService.kt
│           │                   │
│           │                   ├── notification/
│           │                   │   └── NotificationHelper.kt
│           │                   │
│           │                   └── location/
│           │                       └── LocationManager.kt
│           │
│           ├── res/
│           └── AndroidManifest.xml
│
├── gradle/
│   └── libs.versions.toml
│
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 🧩 Tecnologias utilizadas

* **Kotlin**
* **Android**
* **Jetpack Compose**
* **MVVM**
* **Repository Pattern**
* **Room**
* **Retrofit**
* **Gson/Moshi**
* **Navigation Compose**
* **Kotlin Coroutines**
* **Flow**
* **WorkManager**
* **Foreground Service**
* **FusedLocationProviderClient**
* **Android Notifications**

---

## 📦 Como executar o projeto

### 1. Clone o repositório

```bash
git clone URL_DO_REPOSITORIO
```

### 2. Abra o projeto

Abra o projeto no **Android Studio**.

### 3. Aguarde a sincronização do Gradle

O Android Studio realizará o download das dependências necessárias.

### 4. Execute o aplicativo

Execute o projeto em:

* Emulador Android; ou
* Dispositivo Android físico.

### 5. Faça o login

Ao abrir o aplicativo, utilize:

```text
Usuário: admin
Senha: admin123
```

Após a autenticação, o usuário será direcionado para a área de **Personagens**.

---

## 🧪 Teste do modo offline

O funcionamento offline pode ser verificado seguindo estes passos:

1. Abra o aplicativo com internet.
2. Faça login utilizando `admin` / `admin123`.
3. Navegue pelas abas de Personagens, Localizações e Episódios.
4. Aguarde ou execute uma sincronização.
5. Ative o **modo avião**.
6. Feche o aplicativo.
7. Abra o aplicativo novamente.
8. Faça o login.
9. Verifique se os dados previamente sincronizados continuam disponíveis.

Os dados apresentados devem ser provenientes do banco local.

---

## 🔑 Permissões

O aplicativo utiliza permissões relacionadas às funcionalidades de rede, localização, notificações e serviços em primeiro plano.

Entre elas:

```text
INTERNET
ACCESS_FINE_LOCATION
ACCESS_COARSE_LOCATION
FOREGROUND_SERVICE
POST_NOTIFICATIONS
```

As permissões necessárias são solicitadas ao usuário em tempo de execução quando aplicável.

---

## 📂 Organização das camadas

### Data

Responsável pelo acesso aos dados:

* `local/` → Room, entidades e DAOs.
* `remote/` → Retrofit, API e DTOs.
* `repository/` → comunicação entre as fontes de dados.

### Domain

Contém os modelos utilizados pelo domínio da aplicação.

### UI

Contém as telas, ViewModels e navegação do aplicativo.

### Worker

Responsável pela sincronização periódica utilizando WorkManager.

### Service

Responsável pela sincronização manual em Foreground Service.

### Notification

Centraliza a criação e gerenciamento das notificações.

### Location

Responsável pela obtenção da localização do dispositivo.

---

## 🎯 Objetivo do projeto

O projeto foi desenvolvido como uma aplicação Android completa, integrando:

**API → Retrofit → Repository → Room → ViewModel → Jetpack Compose**

com suporte a:

**Login → Navegação → Dados locais → Funcionamento offline → Sincronização → Notificações → Localização**

---

## 📸 Demonstração

> Adicione aqui screenshots das principais telas do aplicativo.

Sugestão:

```text
docs/
├── login.png
├── personagens.png
├── personagem-detalhe.png
├── localizacoes.png
├── localizacao-detalhe.png
├── episodios.png
├── episodio-detalhe.png
└── meu-portal.png
```

Exemplo no README:

```markdown
## 📸 Screenshots

### 🔐 Login
![Tela de Login](docs/login.png)

### 👽 Personagens
![Personagens](docs/personagens.png)

### 🌎 Localizações
![Localizações](docs/localizacoes.png)

### 📺 Episódios
![Episódios](docs/episodios.png)

### 🌀 Meu Portal
![Meu Portal](docs/meu-portal.png)
```

---

## 📌 Status do projeto

**Avaliação — Portal Rick and Morty**

Desenvolvido em Kotlin com Jetpack Compose.

### Credencial de avaliação

```text
Usuário: admin
Senha: admin123
```

> **Para avaliação, utilize a credencial acima para acessar o aplicativo.**
