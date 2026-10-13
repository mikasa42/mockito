package jogo;

/** Lançada quando o servidor de placar não recebe o resultado. */
public class ServidorIndisponivelException extends RuntimeException {

    public ServidorIndisponivelException(String mensagem) {
        super(mensagem);
    }

    public ServidorIndisponivelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
