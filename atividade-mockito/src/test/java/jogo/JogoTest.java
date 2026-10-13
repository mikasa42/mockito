package jogo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JogoTest {

    @Mock private Dado dado;
    @Mock private Jogador jogador1;
    @Mock private Jogador jogador2;
    @Mock private ServidorDePlacar servidor;
    private Jogo jogo;

    @BeforeEach
    void criarJogo() {
        jogo = new Jogo(dado, jogador1, jogador2, servidor);
    }

    @Test
    void r1CadaJogadorLancaUmaVezNaOrdem() {
        when(dado.rolar()).thenReturn(2, 4);

        jogo.jogarRodada();

        InOrder ordem = inOrder(dado, jogador1, jogador2);
        ordem.verify(dado).rolar();
        ordem.verify(jogador1).adicionarPontos(2);
        ordem.verify(jogador1).getPontos();
        ordem.verify(dado).rolar();
        ordem.verify(jogador2).adicionarPontos(4);
        ordem.verify(jogador2).getPontos();
        ordem.verifyNoMoreInteractions();
        verify(dado, times(2)).rolar();
        verifyNoInteractions(servidor);
        assertFalse(jogo.isTerminado());
        assertNull(jogo.getVencedor());
        assertFalse(jogo.isRegistroPendente());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    void r2SomaOValorDoDado(int valor) {
        when(dado.rolar()).thenReturn(valor);

        jogo.jogarRodada();

        verify(jogador1).adicionarPontos(valor);
        verify(jogador2).adicionarPontos(valor);
        verify(jogador1, never()).removerPontos(anyInt());
        verify(jogador2, never()).removerPontos(anyInt());
    }

    @Test
    void r3SeisSomaOnzePontos() {
        when(dado.rolar()).thenReturn(6);

        jogo.jogarRodada();

        verify(jogador1).adicionarPontos(11);
        verify(jogador2).adicionarPontos(11);
        verify(jogador1, never()).removerPontos(anyInt());
        verify(jogador2, never()).removerPontos(anyInt());
    }

    @Test
    void r4UmRemoveTresPontos() {
        when(dado.rolar()).thenReturn(1);

        jogo.jogarRodada();

        verify(jogador1).removerPontos(3);
        verify(jogador2).removerPontos(3);
        verify(jogador1, never()).adicionarPontos(anyInt());
        verify(jogador2, never()).adicionarPontos(anyInt());
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 35})
    void r5PrimeiroJogadorVenceEImpedeSegundaJogada(int pontos) {
        prepararVitoria(jogador1, "Ana", pontos);

        jogo.jogarRodada();

        assertTrue(jogo.isTerminado());
        assertSame(jogador1, jogo.getVencedor());
        verify(dado, times(1)).rolar();
        verifyNoInteractions(jogador2);
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 35})
    void r5SegundoJogadorTambemPodeVencer(int pontos) {
        prepararVitoria(jogador2, "Bruno", pontos);

        jogo.jogarRodada();

        assertTrue(jogo.isTerminado());
        assertSame(jogador2, jogo.getVencedor());
        verify(dado, times(2)).rolar();
    }

    @Test
    void r5VinteENovePontosNaoEncerraOJogo() {
        when(dado.rolar()).thenReturn(2);
        when(jogador1.getPontos()).thenReturn(29);
        when(jogador2.getPontos()).thenReturn(29);

        jogo.jogarRodada();

        assertFalse(jogo.isTerminado());
        assertNull(jogo.getVencedor());
        verify(dado, times(2)).rolar();
        verifyNoInteractions(servidor);
    }

    @Test
    void r6RegistraNomeEPontosUmaUnicaVez() {
        prepararVitoria(jogador1, "Ana", 33);

        jogo.jogarRodada();

        verify(servidor, times(1)).registrarVitoria("Ana", 33);
        verifyNoMoreInteractions(servidor);
        assertFalse(jogo.isRegistroPendente());
    }

    @Test
    void r7FalhaDoServidorMantemVitoriaERegistroPendente() {
        prepararVitoria(jogador1, "Ana", 30);
        doThrow(new ServidorIndisponivelException("Fora do ar"))
                .when(servidor).registrarVitoria("Ana", 30);

        assertDoesNotThrow(jogo::jogarRodada);

        assertTrue(jogo.isTerminado());
        assertSame(jogador1, jogo.getVencedor());
        assertTrue(jogo.isRegistroPendente());
        verify(servidor).registrarVitoria("Ana", 30);
        verify(dado, times(1)).rolar();
        verifyNoInteractions(jogador2);
        assertThrows(IllegalStateException.class, jogo::jogarRodada);
        verify(dado, times(1)).rolar();
    }

    @Test
    void r8ReenvioBemSucedidoRemovePendencia() {
        prepararVitoria(jogador2, "Bruno", 32);
        doThrow(new ServidorIndisponivelException("Fora do ar"))
                .doNothing().when(servidor).registrarVitoria("Bruno", 32);
        jogo.jogarRodada();
        assertTrue(jogo.isRegistroPendente());

        jogo.tentarRegistrarNovamente();

        verify(servidor, times(2)).registrarVitoria("Bruno", 32);
        assertFalse(jogo.isRegistroPendente());
        assertTrue(jogo.isTerminado());
        assertSame(jogador2, jogo.getVencedor());
        assertThrows(IllegalStateException.class, jogo::tentarRegistrarNovamente);
        verifyNoMoreInteractions(servidor);
        verify(dado, times(2)).rolar();
    }

    @Test
    void r8ReenvioComNovaFalhaMantemPendencia() {
        prepararVitoria(jogador1, "Ana", 30);
        doThrow(new ServidorIndisponivelException("Fora do ar"))
                .when(servidor).registrarVitoria("Ana", 30);
        jogo.jogarRodada();

        assertDoesNotThrow(jogo::tentarRegistrarNovamente);

        assertTrue(jogo.isRegistroPendente());
        assertTrue(jogo.isTerminado());
        assertSame(jogador1, jogo.getVencedor());
        verify(servidor, times(2)).registrarVitoria("Ana", 30);
        verify(dado, times(1)).rolar();
    }

    @Test
    void r8SemPendenciaLancaExcecao() {
        assertThrows(IllegalStateException.class, jogo::tentarRegistrarNovamente);

        verifyNoInteractions(dado, jogador1, jogador2, servidor);
    }

    @Test
    void r8RegistroInicialBemSucedidoNaoPodeSerReenviado() {
        prepararVitoria(jogador1, "Ana", 30);
        jogo.jogarRodada();

        assertThrows(IllegalStateException.class, jogo::tentarRegistrarNovamente);

        verify(servidor, times(1)).registrarVitoria("Ana", 30);
        verifyNoMoreInteractions(servidor);
    }

    @Test
    void r9RodadaAposFimLancaExcecaoSemNovasInteracoes() {
        prepararVitoria(jogador1, "Ana", 30);
        jogo.jogarRodada();
        clearInvocations(dado, jogador1, jogador2, servidor);

        assertThrows(IllegalStateException.class, jogo::jogarRodada);

        verifyNoInteractions(dado, jogador1, jogador2, servidor);
    }

    private void prepararVitoria(Jogador jogador, String nome, int pontos) {
        when(dado.rolar()).thenReturn(6);
        when(jogador.getPontos()).thenReturn(pontos);
        when(jogador.getNome()).thenReturn(nome);
    }
}
