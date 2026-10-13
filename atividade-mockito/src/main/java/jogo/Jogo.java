package jogo;

/**
 * Partida de dados entre dois jogadores. As regras estão no enunciado da atividade Mockito.
 */
public class Jogo {

    public static final int PONTOS_PARA_VENCER = 30;
    public static final int BONUS_DO_SEIS = 5;
    public static final int PENALIDADE_DO_UM = 3;

    private final Dado dado;
    private final Jogador jogador1;
    private final Jogador jogador2;
    private final ServidorDePlacar servidor;

    private Jogador vencedor;
    private boolean registroPendente;

    public Jogo(Dado dado, Jogador jogador1,
                Jogador jogador2, ServidorDePlacar servidor) {
        this.dado = dado;
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.servidor = servidor;
    }

    public void jogarRodada() {
        if (vencedor != null) {
            throw new IllegalStateException("O jogo já terminou");
        }
        jogarVez(jogador1);
        if (vencedor == null) {
            jogarVez(jogador2);
        }
    }

    public void tentarRegistrarNovamente() {
        if (!registroPendente) {
            throw new IllegalStateException("Não há registro pendente");
        }
        registrarVitoria();
    }

    public boolean isTerminado() {
        return vencedor != null;
    }

    public Jogador getVencedor() {
        return vencedor;
    }

    public boolean isRegistroPendente() {
        return registroPendente;
    }

    private void jogarVez(Jogador jogador) {
        int valor = dado.rolar();
        if (valor == 1) {
            jogador.removerPontos(PENALIDADE_DO_UM);
        } else if (valor == 6) {
            jogador.adicionarPontos(6 + BONUS_DO_SEIS);
        } else {
            jogador.adicionarPontos(valor);
        }
        if (jogador.getPontos() >= PONTOS_PARA_VENCER) {
            vencedor = jogador;
            registrarVitoria();
        }
    }

    private void registrarVitoria() {
        try {
            servidor.registrarVitoria(vencedor.getNome(), vencedor.getPontos());
            registroPendente = false;
        } catch (ServidorIndisponivelException e) {
            registroPendente = true;
        }
    }
}
