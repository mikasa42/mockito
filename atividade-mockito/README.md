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

## Entrega

Rode `mvn test`, apague a pasta `target`, compacte a pasta `atividade-mockito` inteira
como `mockito_SEUDRE.zip` e envie pelo Moodle.
