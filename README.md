# projetoSicredi
Sistema para ler os dados em um arquivo com etensão CSV e gerar outro arquivo no mesmo formato com os resultados validos para ser enviada a Receita Federal

Este sistema vai ler um arquivo formato csv existente no disco em C:/temp, faz as validações necessarias tais como: formato da agencia, 
conta e os tipos de status validos, ao termino sera apresentada ao usuário uma mensagem de sucesso no console e gera um arquivo na mesma pasta em C:/temp com os resultados, sendo "True" se estiver tudo certo ou "False" caso exista algum erro.

FUNCIONAMENTO:
Existe um arquivo com o nome receita.csv na raiz do projeto, ele deve ser copiado para uma pasta temporaria no computador, no caso C:/temp, ja na pasta o arquivo podera ser substituido por outro com o mesmo nome ou apenas editado seu conteudo, local do arquivo:
C:/temp/receita.csv
Em seguida será gerado na mesma pasta um arquivo com os resultado obtidos, sendo incluido neste arquivo uma coluna "resultado" com os valores true ou false, o nome do arquivo é receitaGerado.csv e estara no seguinte local:
C:/temp/receitaGerado.csv
Ao termino da geracão do arquivo será apresentada a mensagem  no console informado ao usuario que o arquivo foi gerado com sucesso, apenas como meio de informação:
ARQUIVO GERADO COM SUCESSO, VÁ ATE C:/TEMP E VEJA O ARQUIVO COM RESULTADO!!!!

Entrada: ler arquivo receita.csv que estara em C:/temp
Resposta: gera um arquivo receitaGerado.csv, com o resultado de todas as linhas em C:/temp

