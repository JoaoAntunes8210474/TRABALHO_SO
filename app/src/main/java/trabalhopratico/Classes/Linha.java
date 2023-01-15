package trabalhopratico.Classes;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import trabalhopratico.Exceptions.MaxCapacityException;

public class Linha {
    // sentido da linha
    private String sentido;
    // Estações de comboio
    private Estacao[] estacoesAssociadas;
    // Semaforo da linha - might not be doing anything
    private Semaphore semaphore;

    // Construtor
    public Linha(Estacao primeiraEstacao, Estacao segundaEstacao) {
        StringBuilder sb = new StringBuilder("Linha ");
        this.sentido = sb.append(primeiraEstacao.getNome()).append(" - ").append(segundaEstacao.getNome()).toString();
        this.estacoesAssociadas = new Estacao[2];
        this.estacoesAssociadas[0] = primeiraEstacao;
        this.estacoesAssociadas[1] = segundaEstacao;
        this.semaphore = new Semaphore(1);
    }

    // getter do sentido
    public String getSentido() {
        return this.sentido;
    }
    // setter do sentido
    public void setSentido(String sentido) {
        this.sentido = sentido;
    }
    // getter da estacao de chegada
    public Estacao getEstacaoArrival(Estacao estacao) {
        return this.estacoesAssociadas[findEstacaoArrival(estacao)];
    }
    // getter da lista de estacoes
    public Estacao[] getEstacoes() {
        return Arrays.copyOf(this.estacoesAssociadas, 2);
    }
    // getter do semaforo - might not be doing anything
    public Semaphore getSemaphore() {
        return this.semaphore;
    }
    /**
     * Procura na lista de estacoes pela estacao com o mesmo nome da que foi introduzida no parametro e devolve a sua posicao na lista
     * @param estacao - estacao que queremos encontrar
     * @return int i or -1
     */
    public int findEstacao(Estacao estacao) {
        for (int i = 0; i < this.estacoesAssociadas.length; i++) {
            if (this.estacoesAssociadas[i].getNome().equals(estacao.getNome())) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Compara o nome da posicao 0 da lista de estacoes com o nome da estacao introduzida pelo parametro e devolve 1 ou 0 (a posicao da estacao de chegada),
     * isto porque o array tem apenas duas posicoes, partida e chegada, portanto se não é um é o outro !!EXPLICAR MELHOR
     * 
     * Compara o nome da posicao 0 da lista de estacoes com o nome da estacao introduzida pelo parametro
     * Sabendo que a lista tem apenas duas posicoes, ou a comparação é verdadeira e sabemos que a es
     * 
     * @param estacao - estacao cujo destino queremos saber
     * @return 1 or 0
     */
    public int findEstacaoArrival(Estacao estacao) {
        if (this.estacoesAssociadas[0].getNome().equals(estacao.getNome())) {
            return 1;
        }

        return 0;
    }

    public void moveComboioFromDepartureStationToArrivalStation(Estacao estacaoPartida, Comboio comboio)
            throws MaxCapacityException, IOException {
        this.getEstacaoArrival(estacaoPartida).addComboio(comboio);
        this.estacoesAssociadas[findEstacao(estacaoPartida)].removeComboio(comboio);
    }
}