package Classes;

import java.util.ArrayList;
import java.util.Arrays;

import Exceptions.InvalidBilheteException;
import Exceptions.MaxCapacityException;

public class Estacao {

    private static final int MAX_LINHAS = 2;
    //
    private static final int MAX_COMBOIOS = 10;

    private String nome;

    private Linha[] linhas;

    private ArrayList<Comboio> listaComboios;

    private ArrayList<Passageiro> listaPassageiros;
    // Número de passageiros na estação
    private int count;

    /*
     * Construtor da Estação
     */
    public Estacao(String nome) {
        this.nome = nome;
        this.linhas = new Linha[MAX_LINHAS];
        this.listaPassageiros = new ArrayList<>();
        this.count = 0;
    }

    public int getCount() {
        return this.count;
    }

    public String getNome() {
        return this.nome;
    }

    public Linha[] getLinhas() {
        return Arrays.copyOf(this.linhas, MAX_LINHAS);
    }

    public ArrayList<Passageiro> getListaPassageiros() {
        return this.listaPassageiros;
    }

    public ArrayList<Comboio> getListaComboios() {
        return this.listaComboios;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addPassageiro(Passageiro passageiro) {
        this.listaPassageiros.add(passageiro);
    }

    public void movePassageiroToComboio(Passageiro passageiro, Comboio comboio) {
        try {
            this.listaComboios.get(this.listaComboios.indexOf(comboio)).add(passageiro);
            this.listaPassageiros.remove(passageiro);
        } catch (MaxCapacityException e) {
            System.out.println("O passageiro não pode entrar no comboio porque está cheio.");
            e.printStackTrace();
        } catch (InvalidBilheteException e) {
            System.out.println("O passageiro não pode entrar no comboio porque o seu bilhete é inválido.");
            e.printStackTrace();
        }
    }

    public void addComboio(Comboio comboio) throws MaxCapacityException {
        if (this.listaComboios.size() < MAX_COMBOIOS) {
            this.listaComboios.add(comboio);
        } else {
            throw new MaxCapacityException("A estação atingiu o limite de comboios!");
        }

    }

    public void removeComboio(Comboio comboio) {
        this.listaComboios.remove(comboio);
    }

    public void addLinhas(Linha linha) throws MaxCapacityException {
        if (this.count == MAX_LINHAS) {
            throw new MaxCapacityException("A estação atingiu o limite de linhas!");
        }

        this.linhas[this.count++] = linha;
    }
}