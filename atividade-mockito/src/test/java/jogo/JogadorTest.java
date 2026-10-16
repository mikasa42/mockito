package jogo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class JogadorTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, Integer.MAX_VALUE})
    void adicionarPontosNoLimiteNaoPodeTornarPontuacaoNegativa(int quantidade) {
        Jogador jogador = new Jogador("Ana");
        jogador.adicionarPontos(Integer.MAX_VALUE);

        // Rejeitar overflow é aceitável, desde que preserve os pontos anteriores.
        try {
            jogador.adicionarPontos(quantidade);
        } catch (ArithmeticException | IllegalArgumentException e) {
            assertEquals(Integer.MAX_VALUE, jogador.getPontos());
            return;
        }

        assertTrue(jogador.getPontos() >= 0,
                "Somar uma quantidade positiva não pode produzir pontuação negativa: "
                        + jogador.getPontos());
    }

    @Test
    void j1GuardaNomeEComecaComZeroPontos() {
        Jogador jogador = new Jogador("Ana");

        assertEquals("Ana", jogador.getNome());
        assertEquals(0, jogador.getPontos());
    }

    @Test
    void j2AdicionaEAcumulaPontos() {
        Jogador jogador = new Jogador("Ana");

        jogador.adicionarPontos(4);
        jogador.adicionarPontos(11);
        jogador.adicionarPontos(0);

        assertEquals(15, jogador.getPontos());
    }

    @Test
    void j3RemovePontos() {
        Jogador jogador = new Jogador("Ana");
        jogador.adicionarPontos(10);

        jogador.removerPontos(3);
        jogador.removerPontos(0);

        assertEquals(7, jogador.getPontos());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 8})
    void j3PontuacaoNaoFicaNegativa(int quantidade) {
        Jogador jogador = new Jogador("Ana");
        jogador.adicionarPontos(5);

        jogador.removerPontos(quantidade);

        assertEquals(0, jogador.getPontos());
    }

    @Test
    void j3RemoverDeJogadorSemPontosMantemZero() {
        Jogador jogador = new Jogador("Ana");

        jogador.removerPontos(3);

        assertEquals(0, jogador.getPontos());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void j4RejeitaNomeNuloOuEmBranco(String nome) {
        assertThrows(IllegalArgumentException.class, () -> new Jogador(nome));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -3, Integer.MIN_VALUE})
    void j4RejeitaAdicaoNegativaSemAlterarPontos(int quantidade) {
        Jogador jogador = new Jogador("Ana");
        jogador.adicionarPontos(10);

        assertThrows(IllegalArgumentException.class,
                () -> jogador.adicionarPontos(quantidade));

        assertEquals(10, jogador.getPontos());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -3, Integer.MIN_VALUE})
    void j4RejeitaRemocaoNegativaSemAlterarPontos(int quantidade) {
        Jogador jogador = new Jogador("Ana");
        jogador.adicionarPontos(10);

        assertThrows(IllegalArgumentException.class,
                () -> jogador.removerPontos(quantidade));

        assertEquals(10, jogador.getPontos());
    }
}
