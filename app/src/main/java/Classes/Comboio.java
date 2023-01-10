package Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;

import Exceptions.InvalidBilheteException;
import Exceptions.MaxCapacityException;
import Main.Main;

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

    // Construtor
    public Comboio(String nomeComboio, Estacao estacaoPartida, Estacao estacaoChegada, Estacao destinoFinal, LocalTime tempoPartida,
            LocalTime tempoChegada, FileWriter logWriter) {
        this.nomeComboio = nomeComboio;
        this.listaPassageiros = new Passageiro[MAX_PASSAGEIROS];
        this.count = 0;
        this.horario = new Horario(tempoPartida, tempoChegada);
        this.estacaoPartida = estacaoPartida;
        this.estacaoChegada = estacaoChegada;
        this.destinoFinal = destinoFinal;
        this.logWriter = logWriter;
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

        if (passageiro.getBilhete().isValid(this.horario, this.estacaoPartida, this.estacaoChegada)) {
            this.count++;
            this.listaPassageiros[this.count - 1] = passageiro;
        } else {
            throw new InvalidBilheteException("O passageiro tem o bilhete inválido");
        }
    }

    public void removeAll() {
        Arrays.fill(this.listaPassageiros, null);
    }

    public Passageiro get(int integer) {
        return this.listaPassageiros[integer];
    }

    @Override
    public void run() {
        try {
            if (this.estacaoPartida == this.troco.getEstacaoArrival(estacaoPartida)) {
                this.wait();
                this.troco.getSemaphore().acquire();
                System.out.println("A partir da estacao " + this.estacaoPartida.getNome());
                this.estacaoChegada.addComboio(this);
                this.estacaoPartida = this.estacaoChegada;
                Thread.sleep(400);
                System.out.println("A chegar à estacao " + this.estacaoPartida.getNome());
                Thread.sleep(200);
                System.out.println("A desembarcar passageiros...");
                this.estacaoPartida.movePassageirosToEstacao(this);
                this.troco = this.estacaoChegada.getLinhas()[1];
                this.estacaoChegada = this.troco.getEstacaoArrival(estacaoChegada);
                this.horario.getHoraChegada().plusMinutes(30);
                Thread verificarConflitosHorário = new Thread(new HorarioConflictSolver(Main.getAllComboios(), this.logWriter)); 
                verificarConflitosHorário.start();
            } else {
                this.wait();
                this.troco.getSemaphore().acquire();
                System.out.println("A partir da estacao " + this.estacaoPartida.getNome());
                this.estacaoChegada.addComboio(this);
                this.estacaoPartida = this.estacaoChegada;
                Thread.sleep(400);
                System.out.println("A chegar à estacao " + this.estacaoPartida.getNome());
                Thread.sleep(200);
                System.out.println("A desembarcar passageiros...");
                this.estacaoPartida.movePassageirosToEstacao(this);
                this.troco = this.estacaoChegada.getLinhas()[0];
                this.estacaoChegada = this.troco.getEstacaoArrival(estacaoChegada);
                this.horario.getHoraChegada().plusMinutes(30);
                Thread verificarConflitosHorário = new Thread(new HorarioConflictSolver(Main.getAllComboios(), this.logWriter)); 
                verificarConflitosHorário.start();
            }
        } catch (MaxCapacityException e1) {
            System.out.println("A " + this.estacaoPartida.getNome() + " está sobrelotada!!!");
        } catch (InterruptedException | IOException e) {
            
        } finally {
            this.troco.getSemaphore().release();
        }

    }
}