# COS482, atividade Mockito

O enunciado completo está em [atividade_mockito.pdf](atividade_mockito.pdf).

## Abrir o projeto

O projeto Maven fica na pasta `atividade-mockito`, com JUnit 5 e Mockito
declarados no `atividade-mockito/pom.xml`. Requer JDK 17 ou
mais recente.

- **Eclipse:** File, Import, Maven, Existing Maven Projects.
- **IntelliJ IDEA e VS Code:** abra a pasta `atividade-mockito`.
- **Terminal:** na raiz do repositório, execute `mvn -f atividade-mockito/pom.xml test`.

## Onde fica cada coisa

```
atividade-mockito/src/main/java/jogo/    código do jogo (incluindo Jogo e Principal)
atividade-mockito/src/test/java/jogo/    testes de Jogo e Jogador
```

## Refatoração

A classe `Jogo` criava internamente o dado, os dois jogadores e o servidor de
placar. Ela foi refatorada para receber essas dependências pelo construtor:

```java
public Jogo(Dado dado, Jogador jogador1,
            Jogador jogador2, ServidorDePlacar servidor)
```

Essa mudança permite substituir as dependências por mocks do Mockito nos testes,
controlando os resultados do dado, a pontuação dos jogadores e as respostas do
servidor. Assim, os testes são determinísticos e não dependem de sorteio nem de
acesso à rede.

As regras do jogo e os métodos públicos existentes foram preservados. A classe
`Principal` passou a criar as dependências reais e fornecê-las ao novo construtor.
Os testes de `Jogo` cobrem as regras R1 a R9 com mocks, enquanto os testes de
`Jogador` verificam as regras J1 a J4 com objetos reais.

## Entrega

Na pasta `atividade-mockito`, rode `mvn test` e apague a pasta `target`.
Depois, compacte a pasta `atividade-mockito` inteira
como `mockito_SEUDRE.zip` e envie pelo Moodle.
