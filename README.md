## Pré-requisitos

- Docker instalado
- Docker Compose instalado
- Possuir uma conta no hubspot para conseguir realizar a autenticação.
- Possuir uma aplicação criada no hubspot com permissão para o serviço/escopo de criação de contatos.
- Vincular ao webhook da aplicação o seguinte endpoint: https://prompt-vervet-one.ngrok-free.app/api/webhook/hubspot/contact

## Configuração de variáveis de ambiente

O projeto possui um arquivo `.env-example` que deve ser usado como base para criar o arquivo `.env`, necessário para fornecer as variáveis de ambiente utilizadas no projeto.

Na raíz do projeto crie um arquivo `.env`.

Em seguida, edite o arquivo `.env` com os valores apropriados para seu ambiente.

Obs: deixei a variável **NGROK_AUTHTOKEN** setada no `.env-example` para facilitar a execução do projeto, então basta copiá-la para o `.env` que irá criar.

## Build da aplicação

Para construir as imagens definidas no `docker-compose.yml`, execute:

docker-compose build

## Executando a aplicação

Para iniciar a aplicação e o ngrok(necessário pois o **callback** precisa ser realizado com uma url https)

docker-compose up

Esse comando iniciará os serviços definidos no `docker-compose.yml`.

## Acessando a aplicação

Como ja deixei o ngrok com um token pré-setado, a URL utilizada para a api será: https://prompt-vervet-one.ngrok-free.app


