[![Build Status](https://travis-ci.com/danielso2007/starwarsapi.svg?token=cNenT4ixErxehcz1sgqf&branch=master)](https://travis-ci.com/danielso2007/starwarsapi)
![GitHub package version](https://img.shields.io/github/package-json/v/danielso2007/starwarsapi.svg)
[![GitHub pull requests](https://img.shields.io/github/issues-pr-raw/danielso2007/starwarsapi.svg)](https://github.com/danielso2007/starwarsapi/pulls)
[![GitHub issues](https://img.shields.io/github/issues/danielso2007/starwarsapi.svg)](https://github.com/danielso2007/starwarsapi/issues?q=is%3Aopen+is%3Aissue)
![GitHub last commit](https://img.shields.io/github/last-commit/danielso2007/starwarsapi.svg)
[![GitHub issue/pull request author](https://img.shields.io/github/issues/detail/u/danielso2007/starwarsapi/1.svg)](https://github.com/danielso2007/starwarsapi/pulls)
![GitHub contributors](https://img.shields.io/github/contributors/danielso2007/starwarsapi.svg)
![GitHub top language](https://img.shields.io/github/languages/top/danielso2007/starwarsapi.svg)
[![GitHub](https://img.shields.io/github/license/danielso2007/starwarsapi.svg)](https://github.com/danielso2007/starwarsapi)
[![GitHub All Releases](https://img.shields.io/github/downloads/danielso2007/starwarsapi/total.svg)](https://github.com/danielso2007/starwarsapi/archive/master.zip)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg)](https://conventionalcommits.org)


# Starwars API

Projeto em Java e Spring Boot para mostrar todos os conceitos de REST e testes aplicados a um projeto.

Também é apresentado alguns padrõs de builder, singleton e template method para a criação de um framework.

O resultado final  deste projeto e a criação de um framework que seja reutilizado e assim aumentando a produção na criação de uma API baseada em REST.


## Getting Started

Essas instruções fornecerão uma cópia do projeto em execução na sua máquina local para fins de desenvolvimento e teste.

### Prerequisites

Utilizar o ambiente linux e ter o mavem e o java para a compilação e execução do projeto.

Este projeto utiliza o MongoDb como base de dados, para ter um banco de dados para esse projeto, siga o projeto
[dockerMongoDB](https://github.com/danielso2007/dockerMongoDB) que estar em [Docker](https://www.docker.com/) e já com algumas importações de dados.

### Installing

Recomendado instalar o [sdkman](https://sdkman.io/) que é uma ferramenta para gerenciar versões paralelas de vários kits de desenvolvimento de software na maioria dos sistemas baseados em Unix.

Com o sdkman, instale o java:
```
sdk install java 11.0.5-zulu
```

Instalando o Maven:
```
sdk install maven
```

## Running with docker

Há um perfil no `pom.xml` para a criação da imagem do projeto. Ao executar `mvn clean package -P docker`, será realizado o teste e criado o arquivo `Dockerfile` na pasta `target`, criando a imagem `starwarsapi:<project.version>`.
Para ver a imagem criada, digite no terminal o comando `docker images`.

 Para "rodar" a imagem, execute:
```
docker run -p 8080:8080 --name swapi  starwarsapi:<project.version>
```

Teste o endereço:

```
http://localhost:8080/swagger-ui.html#/
```

Inicialmente só é criada a imagem da api. Posteriormente mostrarei como executar o `docker-compose` criado no build, para a execução completa da api com o banco de dados MongoDb.

## Running with docker-compose

Ao executar o maven `mvn clean package -P docker`, é gerado o `Dockerfile` e também o `docker-compose.yml`. Com o docker-compose é possível inicar a aplicação já com um container docker com Mongo. Inicialmente esse banco está vazio.

Execute esse comando dentro da pasta `target`:
```
docker-compose up -d
```

Será iniciado os containers da api e do mongo. Acessando o endereço `http://localhost:8080/api/planets`, será retornado uma lista vazia.

Para parar os containers, execute:
```
docker-compose stop
```

Pelo `pom.xml` é possível configurar a criação do arquivo `docker-compose`. Inicialmente a porta do container mongo a porta está exposta, mas é só modificar o arquivo `docker-compose` e remover, pois a comunicação entre a api e o banco é via `network` interno entre os containers.

## Running (No docker)

Dentro da pasta do projeto, execute:
```
mvn clean package install
```
Após compilar o projeto, dentro da pasta `target`, inicie o projeto (x.x.x é a versão atual do projeto):
```
cd target
nohup java -jar starwarsapi-x.x.x-SNAPSHOT.jar & tail -f nohup.out

```

### Stopping project

Para encerrar o projeto, execute o comando abaixo:
```
ps -ef | grep starwarsapi
```
O comando acima exibirá o id do processo, após ver o id do processo, execute:
```
kill -9 <id_processo>
```

### Scripts de execução e encerramento do projeto

Para iniciar o projeto, execute:
```
./start.sh
```
Para encerrar o projeto, execute:
```
./stop.sh
```

## Test

O projeto está configurado para não executar os testes quando construído para desenvolvimento. Para executar os testes, execute o comando abaixo:

`mvn clean test -P test`

Os testes são executados normalmente quando projeto construído para **produção**. Há no arquivo do `pom.xml` essa configuração, que pode ser modificada a qualquer momento do desenvolvimento. Inicialmente, para o desenvolvimento, o desenvolvedor pode executar seus testes ao seguir o padrão TDD pelo próprio IDE.

## Deployment

Nada será necessário, o projeto é executado como Fat jar.


## Built With

* [Java](https://www.oracle.com/br/java/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Undertow - Servidor Web](http://undertow.io/)
* [Modelmapper - Simple, Intelligent, Object Mapping](http://modelmapper.org/)
* [Project Lombok](https://projectlombok.org/)
* [QueryDsl - type-safe SQL-like queries - fluent API](http://www.querydsl.com/)
* [Swagger](https://swagger.io/)
* [Standard-version](https://github.com/conventional-changelog/standard-version)

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

Usamos [SemVer](http://semver.org/) para versionar. Para as versões disponíveis, consulte as [tags neste repositório](https://github.com/danielso2007/starwarsapi/releases). 

## Authors

* **Daniel Oliveira** - *Initial work* - [danielso2007](https://github.com/danielso2007)

See also the list of [contributors](https://github.com/danielso2007/starwarsapi/graphs/contributors) who participated in this project.

## License

Este projeto está licenciado sob a licença ISC - consulte o arquivo [LICENSE.md](LICENSE.md) para obter detalhes
