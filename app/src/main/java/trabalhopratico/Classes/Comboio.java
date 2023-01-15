package trabalhopratico.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import trabalhopratico.Exceptions.InvalidBilheteException;
import trabalhopratico.Exceptions.MaxCapacityException;

public class Comboio implements Runnable {
    // Número máximo de passageiros no comboio
    private static final int MAX_PASSAGEIROS = 30;
    // Lista de passageiros de um conboio
    private Passageiro[] listaPassageiros;
    // Número de passageiros no comboio
    private int count;
    // Estação de partida do comboio
    private Estacao estacaoPartida;
    // Estacao de chegada do comboio
    private ArrayList<Estacao> estacoesParagem;
    // Estacao de destino do comboio. Pode ser igual à variável estacaoChegada.
    private Estacao destinoFinal;
    // Registo de conflitos
    private FileWriter logWriter;
    // Horário do comboio
    private Horario horario;
    // Direção do comboio
    private Linha troco;
    // Nome do comboio
    private String nomeComboio;

    private int countDelays;

    private boolean acabouViagem;

    // Construtor
    public Comboio(String nomeComboio, Estacao estacaoPartida, Estacao destinoFinal,
            LocalTime tempoPartida, Linha troco, FileWriter logWriter) {
        this.nomeComboio = nomeComboio;
        this.listaPassageiros = new Passageiro[MAX_PASSAGEIROS];
        this.count = 0;
        this.horario = new Horario(tempoPartida, tempoPartida.plusMinutes(30));
        this.estacaoPartida = estacaoPartida;
        this.estacoesParagem = new ArrayList<>();
        this.destinoFinal = destinoFinal;
        this.troco = troco;
        this.logWriter = logWriter;
        this.countDelays = 0;
        this.acabouViagem = false;
    }

    /**
     * Returns the number of passengers
     * 
     * @return number of passengers
     */
    public int getCount() {
        return this.count;
    }

    public int getNumberOfDelays() {
        return this.countDelays;
    }

    protected void incrementNumberOfDelays() {
        this.countDelays++;
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

    public ArrayList<Estacao> getEstacaoParagem() {
        return this.estacoesParagem;
    }

    public Estacao getDestinoFinal() {
        return this.destinoFinal;
    }

    public Horario getHorarioComboio() {
        return this.horario;
    }

    public Linha getDirecaoComboio() {
        return this.troco;
    }

    public String getNomeComboio() {
        return this.nomeComboio;
    }

    public boolean getHasFinished() {
        return this.acabouViagem;
    }

    /**
     * Adds a passenger to the train if his ticket is valid
     * 
     * @param passageiro the passenger to add to the train
     * @throws MaxCapacityException    when the train is full
     * @throws InvalidBilheteException when the passenger doesn't have a valid
     *                                 ticket
     * @throws IOException
     */

    public void add(Passageiro passageiro) throws MaxCapacityException, InvalidBilheteException, IOException {
        if (this.count == MAX_PASSAGEIROS) {
            throw new MaxCapacityException("Comboio cheio!");
        }

        if (passageiro.getBilhete().isValid(this.horario, this)) {
            this.count++;
            this.listaPassageiros[this.count - 1] = passageiro;
        } else {
            throw new InvalidBilheteException("O passageiro tem o bilhete inválido! ");
        }
    }

    public void remove(int indexRemove) {
        for (int i = indexRemove; i < listaPassageiros.length - 1; i++) {
            listaPassageiros[i] = listaPassageiros[i + 1];
        }
        listaPassageiros[listaPassageiros.length - 1] = null;
        this.count--;
    }

    public void removeAll() {
        Arrays.fill(this.listaPassageiros, null);
        this.count = 0;
    }

    public Passageiro get(int integer) {
        return this.listaPassageiros[integer];
    }

    public void addRoute() {
        Comboio tempComboio = new Comboio(this.nomeComboio, this.estacaoPartida, this.destinoFinal,
                this.horario.getHoraPartida(), this.troco, this.logWriter);

        while (!tempComboio.acabouViagem) {
            if (tempComboio.estacaoPartida == tempComboio.troco.getEstacoes()[1]) {
                if (tempComboio.troco.getEstacaoArrival(tempComboio.estacaoPartida) != tempComboio.destinoFinal) {
                    tempComboio.estacaoPartida = tempComboio.troco.getEstacaoArrival(estacaoPartida);
                    tempComboio.getEstacaoParagem().add(tempComboio.estacaoPartida);
                    tempComboio.troco = tempComboio.estacaoPartida.getLinhas()[1];
                } else {
                    tempComboio.acabouViagem = true;
                }
            } else {
                if (tempComboio.troco.getEstacaoArrival(tempComboio.estacaoPartida) != tempComboio.destinoFinal) {
                    tempComboio.estacaoPartida = tempComboio.troco.getEstacaoArrival(tempComboio.estacaoPartida);
                    tempComboio.getEstacaoParagem().add(tempComboio.estacaoPartida);
                    tempComboio.troco = tempComboio.estacaoPartida.getLinhas()[0];
                } else {
                    tempComboio.acabouViagem = true;
                }
            }
        }

        this.estacoesParagem.addAll(tempComboio.estacoesParagem);
    }

    @Override
    public void run() {
        try {
            if (this.estacaoPartida == this.troco.getEstacoes()[1]) {
                System.out.println("[" + Thread.currentThread().getName() + "] - A partir da estacao "
                        + this.estacaoPartida.getNome() + " as "
                        + this.getHorarioComboio().getHoraPartida().toString());
                Thread.sleep(100);
                this.estacaoPartida.removeComboio(this);
                if (this.getEstacaoParagem().size() != 0) {
                    if (this.estacoesParagem.get(0).getAddComboio()) {
                        this.estacoesParagem.get(0).addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A chegar a estacao "
                                + this.estacoesParagem.get(0).getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] - A ignorar estacao...");
                    }
                    if (this.count != 0) {
                        this.estacoesParagem.get(0).movePassageirosToEstacao(this);
                    }
                } else {
                    if (this.destinoFinal.getAddComboio()) {
                        this.destinoFinal.addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A chegar a estacao "
                                + this.destinoFinal.getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] - A ignorar estacao...");
                    }
                    if (this.count != 0) {
                        this.destinoFinal.movePassageirosToEstacao(this);
                    }
                }
                if (this.troco.getEstacaoArrival(estacaoPartida) != this.destinoFinal) {
                    this.estacaoPartida = this.estacoesParagem.get(0);
                    this.troco = this.estacoesParagem.get(0).getLinhas()[1];
                    this.estacoesParagem.remove(0);
                } else {
                    this.acabouViagem = true;
                    this.destinoFinal.removeComboio(this); // O comboio chegou ao fim e agora é removido
                }
            } else {
                // System.out.println("Numero de passageiros: " + this.getCount()); // TEST
                System.out.println("[" + Thread.currentThread().getName() + "] - A partir da estacao "
                        + this.estacaoPartida.getNome() + " as "
                        + this.getHorarioComboio().getHoraPartida().toString());
                Thread.sleep(100);
                this.estacaoPartida.removeComboio(this);
                
                if (this.getEstacaoParagem().size() != 0) {
                    if (this.estacoesParagem.get(0).getAddComboio()) {
                        this.estacoesParagem.get(0).addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A chegar a estacao "
                                + this.estacoesParagem.get(0).getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] - A ignorar estacao...");
                    }
                    if (this.count != 0) {
                        this.estacoesParagem.get(0).movePassageirosToEstacao(this);
                    }
                } else {
                    if (this.destinoFinal.getAddComboio()) {
                        this.destinoFinal.addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A chegar a estacao "
                                + this.destinoFinal.getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + "] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] - A ignorar estacao...");
                    }
                    if (this.count != 0) {
                        this.destinoFinal.movePassageirosToEstacao(this);
                    }
                }

                if (this.troco.getEstacaoArrival(this.estacaoPartida) != this.destinoFinal) {
                    this.estacaoPartida = this.estacoesParagem.get(0);
                    this.troco = this.estacoesParagem.get(0).getLinhas()[0];
                    this.estacoesParagem.remove(0);
                } else {
                    this.acabouViagem = true;
                    this.destinoFinal.removeComboio(this);
                }
            }
            this.horario.setHoraPartida(this.horario.getHoraPartida().plusMinutes(30));
            this.horario.setHoraChegada(this.horario.getHoraChegada().plusMinutes(30));
            Thread.sleep(100);
            System.out.println("[" + Thread.currentThread().getName() + "] - Terminou a viagem!");
        } catch (MaxCapacityException e1) {
            System.out.println("A " + this.estacaoPartida.getNome() + " está sobrelotada!!!");
        } catch (InterruptedException | IOException e) {

        }

    }
}