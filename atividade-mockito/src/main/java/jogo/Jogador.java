package jogo;

/** Um jogador, com nome e pontuação. A pontuação nunca fica negativa. */
public class Jogador {

    private final String nome;
    private int pontos;

    public Jogador(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O jogador precisa de um nome");
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }

    public void adicionarPontos(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade negativa: " + quantidade);
        }
        pontos += quantidade;
    }

    public void removerPontos(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade negativa: " + quantidade);
        }
        pontos = Math.max(0, pontos - quantidade);
    }
}
