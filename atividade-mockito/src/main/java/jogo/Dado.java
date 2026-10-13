package jogo;

import java.util.Random;

/** Dado de seis faces. */
public class Dado {

    private final Random random = new Random();

    /** Devolve um número sorteado entre 1 e 6. */
    public int rolar() {
        return random.nextInt(6) + 1;
    }
}
