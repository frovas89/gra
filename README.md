# golden-raspberry-awards API

API de indicados e vencedores da categoria **Pior Filme** do Golden Raspberry Awards.

## Inicializando a API

Acesse a pasta GRA_Final via prompt de comando e execute o comando:
`java -jar graAPI.jar`

Este comando faz a carga de dados de 'movielist.csv' e a API já inicializa com os dados. O arquivo pode ser alterado para outra massa de dados, porém deve-se manter o nome.

Acesse `localhost:8080/q/swagger-ui/` para verificar os serviços disponíveis

Para executar, basta clicar em **Try it out** em cada serviço do swagger.
Nesse momento ainda aparece opções de como executar via curl no terminal ou a URL pra usar no navegador (ou API client - Postman, Insomnia, etc)

## Executando testes

Acesse a pasta GRA_Final via prompt de comando e execute o comando:
`java -jar graTESTS.jar`

Este comando faz a carga de dados de 'movielistTESTS.csv' e API já inicializa com os dados. Este arquivo não deve ser alterado, pois os testes são válidos somente com estes dados.

## Visualizando os códigos-fontes

Para ver os códigos acesse o repositório: `https://github.com/frovas89/gra`

## Baixando o projeto

Caso queira executar o projeto, basta baixar do repositório e importar como projeto Maven no Eclipse


