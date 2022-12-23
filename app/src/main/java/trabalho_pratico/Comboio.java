package trabalho_pratico;

import java.time.LocalTime;
import java.util.Arrays;

import exceptions.*;

public class Comboio {
    /*
     * Número máximo de passageiros no comboio
     */
    private static final int MAX_PASSAGEIROS = 200;
    /*
     * Lista de passageiros de um conboio
     */
    private Passageiro[] listaPassageiros;
    /*
     * Número de passageiros no comboio
     */
    private int count;

    /*
     * Estação de partida do comboio
     */
    private Estacao estacaoPartida;

    /*
     * Estacao de chegada do comboio
     */
    private Estacao estacaoChegada;

    private Horario horario;

    /*
     * Direção do comboio
     */
    private Linha direcao;

    /*
     * Construtor do comboio
     */
    public Comboio(Estacao estacaoPartida, Estacao estacaoChegada, LocalTime tempoPartida, LocalTime tempoChegada) {
        this.listaPassageiros = new Passageiro[MAX_PASSAGEIROS];
        this.count = 0;
        this.horario = new Horario(tempoPartida, tempoChegada);
        this.estacaoPartida = estacaoPartida;
        this.estacaoChegada = estacaoChegada;
    }

    /**
     * Returns the number of passengers
     * 
     * @return number of passengers
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Returns a copy of the list of passengers
     * 
     * @return copy of the list of passengers
     */
    public Passageiro[] getListaPassageiros() {
        return Arrays.copyOf(this.listaPassageiros, this.listaPassageiros.length);
    }

    /**
     * Returns
     * 
     * @return
     */
    public Estacao getEstacaoPartida() {
        return this.estacaoPartida;
    }

    public Estacao getEstacaoChegada() {
        return this.estacaoChegada;
    }

    public Horario getHorarioComboio() {
        return this.horario;
    }

    public Linha getDirecaoComboio() {
        return this.direcao;
    }

    /**
     * Adds a passenger to the train if his ticket is valid
     * 
     * @param passageiro the passenger to add to the train
     * @throws MaxCapacityException when the train is full
     * @throws InvalidBilheteException when the passenger doesn't have a valid ticket
     */
    public void add(Passageiro passageiro) throws MaxCapacityException, InvalidBilheteException{
        if (this.count == MAX_PASSAGEIROS) {
            throw new MaxCapacityException("Comboio cheio!");
        }

        if (passageiro.getBilhete().isValid(this.horario, this.estacaoPartida, this.estacaoChegada)) {
            this.count++;
            this.listaPassageiros[this.count - 1] = passageiro;
        } else {
            throw new InvalidBilheteException("O passageiro tem o bilhete inválido");
        }
    }

    
}
