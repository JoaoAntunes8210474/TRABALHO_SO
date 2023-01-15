package trabalhopratico.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import trabalhopratico.Exceptions.InvalidBilheteException;
import trabalhopratico.Exceptions.MaxCapacityException;

public class Estacao {
    // Máximo de linha por estação
    private static final int MAX_LINHAS = 2;
    // Máximo de comboios por estação
    private static final int MAX_COMBOIOS = 3;
    // Nome da estação
    private String nome;
    // Array de Linhas
    private Linha[] linhas;
    // ArrayList de Comboios
    private ArrayList<Comboio> listaComboios;
    // ArrayList de Passageiros
    private ArrayList<Passageiro> listaPassageiros;
    // Número de linhas na estação
    private int count;
    // Escrever no ficheiro
    private FileWriter logWriter;
    // Semaforo
    private Semaphore semaphore;
    // Boolean para adicionar comboios a estação
    private boolean addComboio;

    /**
     * Construtor da estação
     * 
     * @param nome nome da estação
     * @param logWriter escritor de conflitos
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

    /**
     * Retorna o número de linhas na estação
     * 
     * @return número de linhas na estação
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Retorna o nome da estação
     * 
     * @return nome da estação
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Retorna uma cópia da lista de linhas
     * 
     * @return cópia da lista de linhas
     */
    public Linha[] getLinhas() {
        return Arrays.copyOf(this.linhas, MAX_LINHAS);
    }

    /**
     * Retorna a lista de passageiros da estação
     * 
     * @return lista de passageiros da estação
     */
    public ArrayList<Passageiro> getListaPassageiros() {
        return this.listaPassageiros;
    }

    /**
     * Retorna a lista de comboios da estação
     * 
     * @return lista de comboios da estação
     */
    public ArrayList<Comboio> getListaComboios() {
        return this.listaComboios;
    }

    /**
     * Retorna o semáforo da estação
     * 
     * @return semáforo da estação
     */
    public Semaphore getSemaphore() {
        return this.semaphore;
    }

    /**
     * Retorna o boolean para indicar quando se pode adicionar comboios à estação 
     * 
     * @return boolean para indicar quando se pode adicionar comboios à estação
     */
    public boolean getAddComboio() {
        return this.addComboio;
    }

    /**
     * Muda o nome da estação para o enviado por referência
     * 
     * @param nome nome da estação enviado por referência
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Adiciona um passageiro passado por referência à lista de passageiros
     * 
     * @param passageiro - passageiro que queremos adicionar à lista de passageiros
     *                   da estação
     */
    public void addPassageiro(Passageiro passageiro) {
        this.listaPassageiros.add(passageiro);
    }

    /**
     * Move o passageiro enviado por parâmetro para o comboio do parametro e remove o passageiro da sua
     * respetiva estação, se o seu bilhete for invalido lança a exceção assim como
     * se o comboio estiver cheio de passageiros
     * 
     * @param passageiro - passageiro que se pretende mover
     * @param comboio - comboio no qual vamos inserir o passageiro
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
     * Move os passageiros do comboio enviado por referência para a estação de chegada que chama este método
     * 
     * @param comboio - comboio do qual vamos remover os passageiros e adicionar à
     *                estação de chegada
     */
    public void movePassageirosToEstacao(Comboio comboio) {
        for (int i = 0; i < comboio.getCount(); i++) {
            if (comboio.getEstacaoParagem().size() != 0) {
                if (comboio.getListaPassageiros()[i].getBilhete().getEstacaoDestino()
                        .equals(comboio.getEstacaoParagem().get(0))) {
                    this.listaPassageiros.add(comboio.getListaPassageiros()[i]);
                    comboio.remove(i);
                    i--;
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
    }

    /**
     * Adiciona o comboio à lista de comboios da estação
     * 
     * @param comboio - comboio que pretendemos adicionar
     * @throws MaxCapacityException - exceção de atingir o limite de comboios numa
     *                              estação
     * @throws IOException - exceção de haver problemas a acessar o ficheiro de conflitos
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
     * Adiciona a linha à lista de linhas da estação
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