# COS482, atividade Mockito

O enunciado completo está no Moodle, em `atividade_mockito.pdf`.

## Abrir o projeto

Projeto Maven, com JUnit 5 e Mockito declarados no `pom.xml`. Requer JDK 17 ou
mais recente.

- **Eclipse:** File, Import, Maven, Existing Maven Projects.
- **IntelliJ IDEA e VS Code:** abrem a pasta direto.
- **Terminal:** `mvn test`.

## Onde fica cada coisa

```
src/main/java/jogo/    o código a refatorar (Jogo e Principal)
src/test/java/jogo/    JogoTest e JogadorTest, onde entram os testes
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

Rode `mvn test`, apague a pasta `target`, compacte a pasta `atividade-mockito` inteira
como `mockito_SEUDRE.zip` e envie pelo Moodle.
