package trabalhopratico.Classes;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import trabalhopratico.Exceptions.MaxCapacityException;

public class Linha {
    // Nome da linha
    private String sentido;
    // Estações de comboio
    private Estacao[] estacoesAssociadas;
    // Número de comboios na linha
    private Semaphore semaphore;

    // Método construtor
    public Linha(Estacao primeiraEstacao, Estacao segundaEstacao) {
        StringBuilder sb = new StringBuilder("Linha ");
        this.sentido = sb.append(primeiraEstacao.getNome()).append(" - ").append(segundaEstacao.getNome()).toString();
        this.estacoesAssociadas = new Estacao[2];
        this.estacoesAssociadas[0] = primeiraEstacao;
        this.estacoesAssociadas[1] = segundaEstacao;
        this.semaphore = new Semaphore(1);
    }

    public String getSentido() {
        return this.sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public Estacao getEstacaoArrival(Estacao estacao) {
        return this.estacoesAssociadas[findEstacaoArrival(estacao)];
    }

    public Estacao[] getEstacoes() {
        return Arrays.copyOf(this.estacoesAssociadas, 2);
    }

    public Semaphore getSemaphore() {
        return this.semaphore;
    }

    public int findEstacao(Estacao estacao) {
        for (int i = 0; i < this.estacoesAssociadas.length; i++) {
            if (this.estacoesAssociadas[i].getNome().equals(estacao.getNome())) {
                return i;
            }
        }

        return -1;
    }

    public int findEstacaoArrival(Estacao estacao) {
        if (this.estacoesAssociadas[0].getNome().equals(estacao.getNome())) {
            return 1; 
        } 

        return 0;
    }

    public void moveComboioFromDepartureStationToArrivalStation(Estacao estacaoPartida, Comboio comboio) throws MaxCapacityException, IOException {
        this.getEstacaoArrival(estacaoPartida).addComboio(comboio);
        this.estacoesAssociadas[findEstacao(estacaoPartida)].removeComboio(comboio);
    }
}