package trabalhopratico.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;

import trabalhopratico.App.Main;
import trabalhopratico.Exceptions.InvalidBilheteException;
import trabalhopratico.Exceptions.MaxCapacityException;

public class Comboio implements Runnable {
    // Número máximo de passageiros no comboio
    private static final int MAX_PASSAGEIROS = 10;
    // Lista de passageiros de um conboio
    private Passageiro[] listaPassageiros;
    // Número de passageiros no comboio
    private int count;
    // Estação de partida do comboio
    private Estacao estacaoPartida;
    // Estacao de chegada do comboio
    private Estacao estacaoChegada;
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

    private boolean acabouViagem;

    // Construtor
    public Comboio(String nomeComboio, Estacao estacaoPartida, Estacao estacaoChegada, Estacao destinoFinal,
            LocalTime tempoPartida,
            LocalTime tempoChegada, Linha troco, FileWriter logWriter) {
        this.nomeComboio = nomeComboio;
        this.listaPassageiros = new Passageiro[MAX_PASSAGEIROS];
        this.count = 0;
        this.horario = new Horario(tempoPartida, tempoChegada);
        this.estacaoPartida = estacaoPartida;
        this.estacaoChegada = estacaoChegada;
        this.destinoFinal = destinoFinal;
        this.troco = troco;
        this.logWriter = logWriter;
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
            this.logWriter.write("O " + passageiro.getName() + " tentou entrar no comboio quando ele ja estava cheio");
            throw new MaxCapacityException("Comboio cheio!");
        }

        if (passageiro.getBilhete().isValid(this.horario, this.estacaoPartida, this.estacaoChegada,
                this.destinoFinal)) {
            this.count++;
            this.listaPassageiros[this.count - 1] = passageiro;
        } else {
            throw new InvalidBilheteException("O passageiro tem o bilhete inválido");
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

    @Override
    public void run() {
        try {
            this.troco.getSemaphore().acquire();
            System.out.println(this.getCount());
            if (this.estacaoPartida == this.troco.getEstacoes()[1]) {
                System.out.println("[" + Thread.currentThread().getName() + "] - A partir da estacao "
                        + this.estacaoPartida.getNome() + " as "
                        + this.getHorarioComboio().getHoraPartida().toString());
                Thread.sleep(2000);
                this.estacaoChegada.addComboio(this);
                Thread.sleep(2000);
                System.out.println("[" + Thread.currentThread().getName() + "] - A chegar a estacao "
                        + this.estacaoChegada.getNome() + " as "
                        + this.getHorarioComboio().getHoraChegada().toString());
                Thread.sleep(1000);
                System.out.println("[" + Thread.currentThread().getName() + "] - A desembarcar passageiros...");
                this.estacaoChegada.movePassageirosToEstacao(this);
                this.troco.getSemaphore().release();
                if (this.troco.getEstacaoArrival(estacaoPartida) != this.destinoFinal) {
                    this.estacaoPartida = this.estacaoChegada;
                    this.troco = this.estacaoChegada.getLinhas()[1];
                    this.estacaoChegada = this.troco.getEstacaoArrival(estacaoPartida);
                } else {
                    this.acabouViagem = true;
                }
                this.horario.setHoraPartida(this.horario.getHoraPartida().plusMinutes(30));
                this.horario.setHoraChegada(this.horario.getHoraPartida().plusMinutes(30));
                // Thread verificarConflitosHorário = new Thread(new
                // HorarioConflictSolver(Main.getAllComboios(), this.logWriter));
                // verificarConflitosHorário.start();
                Thread.sleep(2000);
                System.out.println("[" + Thread.currentThread().getName() + "] - Terminou a viagem!");
            } else {
                System.out.println("Numero de passageiros: " + this.getCount()); // TEST
                System.out.println("[" + Thread.currentThread().getName() + "] - A partir da estacao "
                        + this.estacaoPartida.getNome() + " as "
                        + this.getHorarioComboio().getHoraPartida().toString());
                Thread.sleep(2000);
                this.estacaoChegada.addComboio(this);
                Thread.sleep(2000);
                System.out.println("[" + Thread.currentThread().getName() + "] - A chegar a estacao "
                        + this.estacaoChegada.getNome() + " as "
                        + this.getHorarioComboio().getHoraChegada().toString());
                Thread.sleep(1000);
                System.out.println("[" + Thread.currentThread().getName() + "] - A desembarcar passageiros...");
                this.estacaoChegada.movePassageirosToEstacao(this);
                this.troco.getSemaphore().release();
                if (this.troco.getEstacaoArrival(estacaoPartida) != this.destinoFinal) {
                    this.estacaoPartida = this.estacaoChegada;
                    this.troco = this.estacaoChegada.getLinhas()[0];
                    this.estacaoChegada = this.troco.getEstacaoArrival(estacaoChegada);
                } else {
                    this.acabouViagem = true;
                }
                this.horario.setHoraPartida(this.horario.getHoraPartida().plusMinutes(30));
                this.horario.setHoraChegada(this.horario.getHoraPartida().plusMinutes(30));
                // Thread verificarConflitosHorário = new Thread(new
                // HorarioConflictSolver(Main.getAllComboios(), this.logWriter));
                // verificarConflitosHorário.start();
                Thread.sleep(2000);
                System.out.println("[" + Thread.currentThread().getName() + "] - Terminou a viagem!");
            }
        } catch (MaxCapacityException e1) {
            System.out.println("A " + this.estacaoPartida.getNome() + " está sobrelotada!!!");
        } catch (InterruptedException | IOException e) {

        }

    }
}