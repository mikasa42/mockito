package jogo;

/** Joga uma partida inteira e mostra o resultado. */
public class Principal {

    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        int rodadas = 0;
        while (!jogo.isTerminado()) {
            jogo.jogarRodada();
            rodadas++;
        }
        Jogador vencedor = jogo.getVencedor();
        System.out.println("Vencedor: " + vencedor.getNome() + ", com " + vencedor.getPontos()
                + " pontos, em " + rodadas + " rodadas");
        if (jogo.isRegistroPendente()) {
            System.out.println("O placar online não recebeu o resultado.");
        }
    }
}
