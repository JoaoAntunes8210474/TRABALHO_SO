package trabalhopratico.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import trabalhopratico.Exceptions.InvalidBilheteException;
import trabalhopratico.Exceptions.MaxCapacityException;

public class Estacao {
    // máximo de linha por estação
    private static final int MAX_LINHAS = 2;
    // máximo de comboios por estação
    private static final int MAX_COMBOIOS = 3;
    // nome da estação
    private String nome;
    // array de Linhas
    private Linha[] linhas;
    // ArrayList de Comboios
    private ArrayList<Comboio> listaComboios;
    // ArrayList de Passageiros
    private ArrayList<Passageiro> listaPassageiros;
    // Número de passageiros na estação
    private int count;
    // Escrever no ficheiro
    private FileWriter logWriter;
    // Semaforo
    private Semaphore semaphore;
    // Boolean para adicionar comboios a estação
    private boolean addComboio;

    // Construtor
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

    /**
     * Return the number 
     * @return
     */
    public int getCount() {
        return this.count;
    }

    // getter do nome
    public String getNome() {
        return this.nome;
    }

    // getter do array de linhas
    public Linha[] getLinhas() {
        return Arrays.copyOf(this.linhas, MAX_LINHAS);
    }

    // getter da lista de passageiros
    public ArrayList<Passageiro> getListaPassageiros() {
        return this.listaPassageiros;
    }

    // getter da lista de comboios
    public ArrayList<Comboio> getListaComboios() {
        return this.listaComboios;
    }

    // getter do semaforo
    public Semaphore getSemaphore() {
        return this.semaphore;
    }

    // getter do boolean para adicionar comboios
    public boolean getAddComboio() {
        return this.addComboio;
    }

    // setter do nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Adiciona um passageiro pelo parametro à lista de passageiros
     * 
     * @param passageiro - passageiro que queremos adicionar à lista de passageiros
     *                   da estação
     */
    public void addPassageiro(Passageiro passageiro) {
        this.listaPassageiros.add(passageiro);
    }

    /**
     * Move o passageiro do parametro para o comboio do parametro e remove-o da sua
     * respetiva estação, se o seu bilhete for invalido lança a exceção assim como
     * se o comboio estiver cheio de passageiros
     * 
     * @param passageiro - passageiro que se pretende mover
     * @param comboio    - comboio no qual vamos inserir o passageiro
     */
    public void movePassageiroToComboio(Passageiro passageiro, Comboio comboio) {
        try {
            Thread.sleep(300);
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
        } catch (IOException | InterruptedException e) {
        } 
    }

    /**
     * Move os passageiros do parametro comboio para a respetiva estação de chegada
     * 
     * @param comboio - comboio do qual vamos remover os passageiros e adicionar à
     *                estação de chegada
     */
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

    /**
     * Adiciona o comboio a uma lista de comboios da estação
     * 
     * @param comboio - comboio que pretendemos adicionar
     * @throws MaxCapacityException - exceção de atingir o limite de comboios numa
     *                              estação
     * @throws IOException
     */
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

    /**
     * Remove o comboio da lista de comboios da estação
     * 
     * @param comboio - comboio que pretendemos remover
     */
    public void removeComboio(Comboio comboio) {
        this.listaComboios.remove(comboio);
    }

    /**
     * Adciona a linha à lista de linhas da estação
     * 
     * @param linha - linha a adicionar
     * @throws MaxCapacityException - exceção do limite de linhas
     */
    public void addLinhas(Linha linha) throws MaxCapacityException {
        if (this.count == MAX_LINHAS) {
            throw new MaxCapacityException("A estação atingiu o limite de linhas!");
        }

        this.linhas[this.count++] = linha;
    }
}