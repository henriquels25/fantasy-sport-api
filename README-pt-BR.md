[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=code_smells)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=coverage)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=ncloc)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=security_rating)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=sqale_index)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)

Fantasy Sport API
===============
Aplicação para gerenciar aplicações Fantasy, possibilitando aos usuários procurar jogadores, escalar os seus times
e participar de campeonatos.

## Versão em inglês
Este documento também está disponível em [:us: Inglês](README.md).

## Propósito
Esta aplicação serve para propósitos educacionais e o objetivo é mostrar boas práticas de 
arquitetura de software e testes.

## Tecnologia
Este projeto é escrito utilizando a plataforma  [JDK 15](https://jdk.java.net/15/), [Spring Boot](https://spring.io/projects/spring-boot)
com o framework reativo web [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html) 
 e [MongoDB](https://www.mongodb.com/) como banco de dados.

## Arquitetura
Esta aplicação utiliza [Arquitetura Hexagonal](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))
como estilo arquitetura.

Seguindo isto, os módulos da aplicação são dividos em três partes:

* **Lógica de negócio (Core)**: Código que contém lógicas de negócio puras, com o mínimo de acoplamento
com frameworks, banco de dados ou sistemas de mensageria. Sempre que a aplicação precisar se comunicar com o 
mundo externo, o core deve depender de uma interface implementada por um adaptador secundário.

* **Adaptadores primários**: Código que dirige a aplicação. Os adaptadores primários devem depender das portas primárias
definidas na camada de lógica de negócio.

* **Adaptadores secundários**: Código que possibilita a aplicação conversar com o mundo externo. Adaptadores secundários
devem implementar portas secundárias definidas no core da aplicação.

### Organização dos pacotes
O projeto é dividido em módulos de acordo com casos de uso do negócio. Cada módulo
possui seu próprio pacote dentro do pacote raiz `io.henriquels25.fantasysport`.

Exemplo:
O módulo responsável por gerenciar os jogadores é `io.henriquels25.fantasysport.player`.

#### Lógica de negócio (core)

Dentro de um módulo, o código relacionado a lógica de negócio (core) é colocado na
raiz do pacote.

Exemplo: O `PlayerFacade` é o ponto de entrada para o core da aplicação no módulo de Jogadores
e é colocado em `io.henriquels25.fantasysport.player`.

#### Adaptadores
Os adaptadores são colocados no pacote chamado `infra` dentro do módulo.

Exemplo:
Os adaptadores para o módulo de jogadores são colocados em `io.henriquels25.fantasysport.player.infra`.

Os adaptadores são colocados em pacotes de acordo com o seu tipo. Controllers são colocados
em `**.infra.controller`, adaptadores para documentos Mongo em `**.infra.mongo`, etc.

##### Adaptadores Primários
Adaptadores primários devem depender em um ponto de entrada (porta primária) para a 
lógica de negócio (core) do módulo.

Exemplo:
O adaptador primário `PlayerController` depende na interface `PlayerOperations` (ponto de entrada para a lógica de negócio).

##### Adaptadores Secundários
Adaptadores secundários devem implementar portas de saída definidas na lógica 
de negócio (core) do módulo.

Exemplo:
O adaptadores secundário `MongoPlayerRepository` implementa a porta secundária `PlayerRepository`.

## Testes
Este projeto possui três níveis de teste:

* **Teste unitário**: Cobre a lógica de negócio dos módulos. São extremamente rápidos
e não dependem de nenhum framework (diferente de JUnit5/Mockito), banco de dados
ou sistema de mensageria.

* **Teste de integração**: Cobre os adaptadores dos módulos (tudo que estiver dentro do
pacote `infra`). Estes testes são mais lentos que os unitários porque é necessário um
contexto de aplicação, um banco de dados ou outras tecnologias.
Normalmente, anotações como  `@WebFluxTest`, `@DataMongoTest` e outras são utilizadas.
 
* **Teste de aceitação**: Este tipo de teste configura toda a aplicação junto com as 
dependências externas (banco de dados, sistema de mensageria, APIs externas) localmente e
os testes são escritos a partir da perspectiva do usuário, como casos de uso.
Normalmente, a anotação `@SpringBootTest` é usada para este tipo de teste.

Para cobertura de código, apenas testes unitários e de integração devem ser considerados.

### Anotações especiais
Para ter tasks Gradle separadas para cada tipo de teste, as seguintes anotações devem
ser utilizadas ao definir testes:

* **Teste unitário**: `@Test` (org.junit.jupiter.api.Test)
* **Teste de integração**: `@IntegrationTest` (io.henriquels25.fantasysport.annotations.IntegrationTest)
* **Teste de aceitação**: `@AcceptanceTest` (io.henriquels25.fantasysport.annotations.AcceptanceTest)

### Tasks Gradle
As seguintes tasks Gradle estão disponíveis para executar os testes:
* **Testes unitários**: `./gradlew unitTest`
* **Testes de integração**: `./gradlew integrationTest`
* **Testes de aceitação**: `./gradlew acceptanceTest`

### Cobertura de testes
O [Plugin JaCoCo](https://docs.gradle.org/current/userguide/jacoco_plugin.html) está configurado no projeto
para habilitar a análise de cobertura de testes.

O comando `./gradlew jacocoTestReport` gera relatórios de cobertura de testes em XML/HTML
dos testes executados anteriormente.
 
Os relatórios ficam disponíveis no diretório `build/reports/jacoco`.

Por exemplo, para visualizar a cobertura dos testes unitário e de integração combinados, 
os seguintes comandos devem ser executados:

- `./gradlew unitTest`
- `./gradlew integrationTest`
- `./gradlew jacocoTestReport`

## Sonarcloud
Este projeto está disponívem no [Sonarcloud](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api).

## Pipeline
Este reposótirio utiliza [GitHub Actions](https://docs.github.com/en/free-pro-team@latest/actions) para automatização
da pipeline CI/CD.

Atualmente, a pipeline executa para "pushes" em todas as branches, executando os seguintes
passos:

- Testes unitários
- Testes de integração
- Relatório de cobertura de testes e análise Sonar
- Testes de aceitação