
## Observação:
**Deixei um arquivo `.env` montado com informações sensíveis relacionadas a autenticação, prática que nunca deve ser feita e estou deixando aqui apenas para facilitar o teste, caso quem execute não possua uma aplicação criada com as informações disponibilizadas neste documento.
Em uma situação normal o certo seria seguir o .env-example e criar seu proprio .env**

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
  
## Build da aplicação

  Para construir as imagens definidas no `docker-compose.yml`, execute:
  
  docker-compose build

## Executando a aplicação

  Para iniciar a aplicação e o ngrok(necessário pois o **callback** precisa ser realizado com uma url https):
  
  docker-compose up

## Acessando a aplicação

  Como já deixei o ngrok com um token pré-setado, basta utilizar os endpoints com a seguinte URL: https://prompt-vervet-one.ngrok-free.app


## Endpoints

  #### Para autenticação: 
  
  **GET**
  https://prompt-vervet-one.ngrok-free.app/api/oauth/authorize
  
  Este endpoint retorna um link para que você possa realizar a autenticação na plataforma do HubSpot.
  Após concluir esse processo, via callback uma mensagem de sucesso será exibida juntamente com o token gerado.
  
  Importante:
  O token exibido é apenas informativo — você pode simplesmente fechar a página após visualizar a mensagem de sucesso.
  A partir desse momento, sua aplicação já estará autenticada e pronta para utilizar o endpoint de criação de contatos normalmente.

#### Para criar um contato:

  **POST**
  https://prompt-vervet-one.ngrok-free.app/api/contacts
  
  Limitei o uso da api pra criar contatos utilizando somente os atributos `email, lastname, firstname`, como no exemplo de requisição abaixo:
  
    curl --location --request POST 'https://prompt-vervet-one.ngrok-free.app/api/contacts' \
    --header 'Content-Type: application/json' \
    --data-raw '{
      "properties": {
        "email": "teste@gmail.com",
        "lastname": "testesobrenome",
        "firstname": "teste"
      }
    }'
  
  
