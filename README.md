ES-2024_25-2Sem-Terça-LEI-PL-D
   
    Francisco Rodrigues dos Santos, 111015, CiscosProgramming
    David Prezado, 111248, DMPos
    Afonso Alves, 111157, Alves26Programador

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ES-Projeto_ES-2024_25-2Sem-Terca-LEI-PL-D&metric=alert_status)](https://sonarcloud.io/dashboard?id=ES-Projeto_ES-2024_25-2Sem-Terca-LEI-PL-D)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ES-Projeto_ES-2024_25-2Sem-Terca-LEI-PL-D&metric=coverage)](https://sonarcloud.io/dashboard?id=ES-Projeto_ES-2024_25-2Sem-Terca-LEI-PL-D)

[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=ES-Projeto_ES-2024_25-2Sem-Terca-LEI-PL-D&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=ES-Projeto_ES-2024_25-2Sem-Terca-LEI-PL-D)

Funcionalidades Implementadas:
    
    1: Permita carregar os dados do cadastro de propriedades rústicas em Portugal, a partir das fontes de dados
    publicamente disponíveis, por exemplo, em https://www.ifap.pt/isip/ows/. É fornecido um ficheiro em formato CSV
    (Comma-Separated Value) no Moodle (“Madeira-Moodle.csv”), com dados do cadastro de propriedades rústicas da região
    autónoma da Madeira, que pode ser usado como fonte de dados no desenvolvimento e teste da aplicação;

    2: Permita representar sob a forma de um grafo o cadastro de propriedades rústicas, onde os nós representam as
    propriedades e as arestas representam as relações de adjacência entre os vértices (entre as propriedades);

    3: Permita representar sob a forma de um grafo o conjunto dos proprietários, onde os nós representam os proprietários
    e as arestas representam as relações de vizinhança entre proprietários;

    4:  Permita calcular a área média das propriedades, de uma área geográfica/administrativa indicada pelo utilizador
    (freguesia, concelho, distrito);


Identificação e Descrição de Erros:
    
    De momento não existem erros no código.


Funcionalidades Não Implementadas:

    5:  Permita calcular a área média das propriedades, assumindo que propriedades adjacentes, do mesmo proprietário,
       devem ser consideradas como uma única propriedade, para uma área geográfica/administrativa indicada pelo utilizador;

    6: Permita gerar sugestões para troca de propriedades entre proprietários, que maximizem a área média das
       propriedades por proprietário (área média entendida segundo o cálculo descrito no ponto 5). Na sugestão de trocas,
       deve ser considerado não só o aumento de área média proporcionado por essa troca, mas também o potencial da troca
       ser realizada pelos proprietários (assume-se que os proprietários envolvidos nas trocas, têm interesse em incorrer nos
       menores custos possíveis para realizar a transação). Por exemplo, a sugestão de troca de uma propriedade de 10 m2
       por outra propriedade de 11 m2 entre dois proprietários, tem mais potencial de ser realizada, do que a sugestão de
       troca de uma propriedade de 10 m2 por outra propriedade de 1000 m2. A área deverá por isso ser uma característica
       considerada na avaliação do potencial de troca, no processo de sugestão de trocas da aplicação a desenvolver;

    7:  Em geral, trocas entre propriedades com características (valor) similares, têm mais potencial de ser realizadas. Entre
       as possíveis caraterísticas a considerar encontram-se a distância a vias de comunicação, distância a zonas urbanas, etc.
       O grupo deverá definir as características a usar no processo de comparação do valor das propriedades, devendo
       considerar pelo menos a área (mencionada no ponto 6) e mais duas características à escolha do grupo (não
       necessariamente as indicadas anteriormente, distância a vias de comunicação e distância a zonas urbanas).

Relatório de avaliação de qualidade do software:

        A avaliação da qualidade do software foi feita através da ferramenta SonarCloud. Estes são alguns dados relativamente ao
        relatório gerado pela ferramenta:
        Coverage: 75.5% 
        Security: 0 issues
        Reliability: Sem bugs (0 issues)
        Maintainability: 37 code smells (nível A)
        Duplications: 0%
        Linhas analisadas: 226
[Ver análise completa (Overall Code)](https://sonarcloud.io/summary/overall?id=ES-Projeto_ES-2024_25-2Sem-Terca-LEI-PL-D)

  

