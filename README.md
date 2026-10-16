# COS482, atividade Mockito

O enunciado completo está em [atividade_mockito.pdf](atividade_mockito.pdf).

## Abrir o projeto

O projeto Maven fica na pasta `atividade-mockito`, com JUnit 5 e Mockito
declarados no `atividade-mockito/pom.xml`. Requer JDK 17 ou
mais recente.

- **Eclipse:** File, Import, Maven, Existing Maven Projects.
- **IntelliJ IDEA e VS Code:** abra a pasta `atividade-mockito`.
- **Terminal:** na raiz do repositório, execute `mvn -f atividade-mockito/pom.xml test`.

### Ambiente Java isolado

O JDK 17 local fica em `.venv/jdk`, e o cache de dependências Maven em
`.venv/m2`. Esses diretórios são ignorados pelo Git. Para executar os testes
com esse ambiente, use na raiz do repositório:

```bash
./mvn-local test
```

O script define `JAVA_HOME` e `PATH` apenas para o processo Maven, sem alterar
a configuração de outros projetos ou do terminal.

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

## Balanço dos testes

Na execução de `./mvn-local test`, a suíte original passou com 37 testes.
Após adicionar casos de limite, o resultado foi **41 testes executados,
37 aprovados, 4 falhas, nenhum erro e nenhum teste ignorado** (`BUILD FAILURE`).

| Classe | Executados | Aprovados | Falhas |
| --- | ---: | ---: | ---: |
| `JogadorTest` | 21 | 18 | 3 |
| `JogoTest` | 20 | 19 | 1 |
| **Total** | **41** | **37** | **4** |

As quatro falhas revelam dois problemas:

- **Overflow na pontuação:** o teste parametrizado
  `adicionarPontosNoLimiteNaoPodeTornarPontuacaoNegativa` começa com
  `Integer.MAX_VALUE` pontos e adiciona `1`, `2` ou `Integer.MAX_VALUE`.
  Os resultados são, respectivamente, `-2147483648`, `-2147483647` e `-2`.
  Isso viola a garantia de que a pontuação nunca fica negativa. O teste aceita
  a rejeição da soma por exceção, desde que a pontuação anterior seja preservada.
- **Reenvio de resultado alterado:** o teste
  `r8ReenviaResultadoOriginalMesmoSePontuacaoDoJogadorMudar` simula uma vitória
  com 30 pontos e uma falha do servidor. Antes do reenvio, a pontuação consultada
  no jogador muda para 40. O código envia 40, enquanto o teste espera o resultado
  original de 30. Esse caso interpreta a regra R8 como o reenvio do resultado
  da vitória, mesmo que o jogador seja alterado externamente depois.

O código de produção não foi alterado para corrigir esses problemas. Os novos
testes permanecem ativos e reproduzem as falhas; os 37 testes anteriores continuam
passando. A execução completa foi confirmada fora do sandbox, pois a restrição
de instrumentação do Mockito no sandbox causava erros de ambiente.
