package trabalhopratico.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import trabalhopratico.Exceptions.InvalidBilheteException;
import trabalhopratico.Exceptions.MaxCapacityException;

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
    //
    private FileWriter logWriter;

    private Semaphore semaphore;

    private boolean addComboio;

    /*
     * Construtor da Estação
     */
    public Estacao(String nome, FileWriter logWriter) {
        this.nome = nome;
        this.linhas = new Linha[MAX_LINHAS];
        this.listaPassageiros = new ArrayList<>();
        this.listaComboios = new ArrayList<>();
        this.count = 0;
        this.logWriter = logWriter;
        this.semaphore = new Semaphore(1);
        this.addComboio = true;
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

    public Semaphore getSemaphore() {
        return this.semaphore;
    }

    public boolean getAddComboio() {
        return this.addComboio;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addPassageiro(Passageiro passageiro) {
        this.listaPassageiros.add(passageiro);
    }

    // o ideal era termos um run que faz isto
    public void movePassageiroToComboio(Passageiro passageiro, Comboio comboio) {
        try {
            comboio.add(passageiro);
            this.listaPassageiros.remove(passageiro);
        } catch (MaxCapacityException e) {
            try {
                this.logWriter.write("[Comboio Sobrelotado Conflict Solver] - O " + passageiro.getName()
                        + " tentou entrar no " + comboio.getNomeComboio() + ", no entanto, ele já estava cheio.\n");
            } catch (IOException e1) {
            }

        } catch (InvalidBilheteException e) {
            System.out.println("O passageiro não pode entrar no comboio porque o seu bilhete é inválido.");
        } catch (IOException e) {
        }
    }

    public void movePassageirosToEstacao(Comboio comboio) {
        /*
         * try {
         * this.semaphore.acquire();
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         */
        for (int i = 0; i < comboio.getCount(); i++) {
            // System.out.println(comboio.getListaPassageiros()[i].getName());
            if (comboio.getEstacaoParagem().size() != 0) {
                if (comboio.getListaPassageiros()[i].getBilhete().getEstacaoDestino()
                        .equals(comboio.getEstacaoParagem().get(0))) {
                    this.listaPassageiros.add(comboio.getListaPassageiros()[i]);
                    comboio.remove(i);
                    i--;
                    // System.out.println("Tamanho da lista de comboios:
                    // "+this.listaComboios.size());
                }
            } else {
                if (comboio.getListaPassageiros()[i].getBilhete().getEstacaoDestino()
                        .equals(comboio.getDestinoFinal())) {
                    this.listaPassageiros.add(comboio.getListaPassageiros()[i]);
                    comboio.remove(i);
                    i--;
                }
            }
        }
        // this.semaphore.release();
    }

    public void addComboio(Comboio comboio) throws MaxCapacityException, IOException {
        if (this.listaComboios.size() < MAX_COMBOIOS) {
            this.listaComboios.add(comboio);
        } else {
            this.addComboio = false;
            this.logWriter.write("[Estação Sobrelotada Conflict Solver] - O " + comboio.getNomeComboio()
                    + " tentou entrar na " + this.nome + ", no entanto, ela já estava cheia.\n");
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