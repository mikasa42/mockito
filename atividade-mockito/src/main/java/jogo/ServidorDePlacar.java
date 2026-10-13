package jogo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Envia o resultado de uma partida para o placar online.
 *
 * O endereço usa o domínio reservado .invalid, que nunca existe. Rodar este
 * código de verdade sempre termina em ServidorIndisponivelException, como
 * aconteceria com um servidor fora do ar.
 */
public class ServidorDePlacar {

    private static final URI ENDERECO = URI.create("https://placar.cos482.invalid/vitorias");

    private final HttpClient cliente = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public void registrarVitoria(String nome, int pontos) {
        String corpo = "{\"nome\":\"" + nome + "\",\"pontos\":" + pontos + "}";
        HttpRequest requisicao = HttpRequest.newBuilder(ENDERECO)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpo))
                .build();
        try {
            HttpResponse<Void> resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.discarding());
            if (resposta.statusCode() >= 300) {
                throw new ServidorIndisponivelException("Placar respondeu " + resposta.statusCode());
            }
        } catch (IOException e) {
            throw new ServidorIndisponivelException("Placar fora do ar", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServidorIndisponivelException("Envio interrompido", e);
        }
    }
}
