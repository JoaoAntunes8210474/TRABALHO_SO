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
    // Estações de chegada do comboio
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
    // número de delays que o comboio sofre
    private int countDelays;
    // boolean de acabar a viagem
    private boolean acabouViagem;

    /**
     * Construtor do comboio
     * 
     * @param nomeComboio nome do comboio
     * @param estacaoPartida estação de partida do comboio
     * @param destinoFinal estação de destino do comboio
     * @param tempoPartida hora de partida do comboio
     * @param troco troço por onde passa o comboio
     * @param logWriter escritor de conflitos
     */
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
     * Retorna o máximo de passageiros no comboio
     * @return número máximo de passageiros no comboio
     */
    public int getMax() {
        return this.MAX_PASSAGEIROS;
    }

    /**
     * Retorna o número de passageiros no comboio
     * 
     * @return número de passageiros no comboio
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Retorna o número de atrasos que o comboio teve
     * 
     * @return número de atrasos que o comboio teve
     */
    public int getNumberOfDelays() {
        return this.countDelays;
    }

    /**
     * Incrementa o número de atrasos que o comboio teve
     */
    protected void incrementNumberOfDelays() {
        this.countDelays++;
    }

    /**
     * Retorna uma cópia da lista de passageiros
     * 
     * @return cópia da lista de passageiros
     */
    public Passageiro[] getListaPassageiros() {
        return Arrays.copyOf(this.listaPassageiros, this.listaPassageiros.length);
    }

    /**
     * Retorna a estação de partida do comboio
     * 
     * @return estação de partida do comboio
     */
    public Estacao getEstacaoPartida() {
        return this.estacaoPartida;
    }

    /**
     * Retorna a lista de paragens intermédias da viagem do comboio
     * 
     * @return lista de paragens intermédias da viagem do comboio
     */
    public ArrayList<Estacao> getEstacaoParagem() {
        return this.estacoesParagem;
    }

    /**
     * Retorna a estação de destino do comboio
     * @return estação de destino do comboio
     */
    public Estacao getDestinoFinal() {
        return this.destinoFinal;
    }

    /**
     * Retorna o horário do comboio
     * 
     * @return horário do comboio
     */
    public Horario getHorarioComboio() {
        return this.horario;
    }

    /**
     * Retorna o troço do comboio
     * 
     * @return troço do comboio
     */
    public Linha getTrocoComboio() {
        return this.troco;
    }

    /**
     * Retorna o nome do comboio
     * 
     * @return nome do comboio
     */
    public String getNomeComboio() {
        return this.nomeComboio;
    }

    /**
     * Retorna uma indicação de se o comboio já terminou a sua viagem
     * 
     * @return indicação de se o comboio já terminou a sua viagem
     */
    public boolean getHasFinished() {
        return this.acabouViagem;
    }

    /**
     * Adiciona um passageiro ao comboio se ele tiver o bilhete válido
     * 
     * @param passageiro passageiro a adicionar ao comboio
     * @throws MaxCapacityException quando o comboio atingiu o limite de passageiros
     * @throws InvalidBilheteException quando o passageiro tem um bilhete inválido
     * @throws IOException quando houve problema a acessar o ficheiro de escrever conflitos
     */

    public void add(Passageiro passageiro) throws MaxCapacityException, InvalidBilheteException, IOException {
        if (this.count == MAX_PASSAGEIROS) {
            throw new MaxCapacityException("[" + this.nomeComboio + "("+this.getCount() + "/" + this.getCount() + ")] - CHEIO de passageiros");
        }

        if (passageiro.getBilhete().isValid(this.horario, this)) {
            this.count++;
            this.listaPassageiros[this.count - 1] = passageiro;
        } else {
            throw new InvalidBilheteException("[" + this.nomeComboio + "] - Passageiro \'" + passageiro.getNif() + "\' tem o bilhete inválido!");
        }
    }

    /**
     * Remove o passageiro da lista na posição recebida por referência e ajusta as posições seguintes
     * 
     * @param indexRemove posição na lista do passageiro a remover
     */
    public void remove(int indexRemove) {
        for (int i = indexRemove; i < listaPassageiros.length - 1; i++) {
            listaPassageiros[i] = listaPassageiros[i + 1];
        }
        listaPassageiros[listaPassageiros.length - 1] = null;
        this.count--;
    }

    /**
     * Retorna o passageiro da lista de passageiros na posição recebida por referência
     * 
     * @param integer posição do passageiro na lista
     * @return o passageiro da lista de passageiros na posição recebida por referência 
     */
    public Passageiro get(int integer) {
        return this.listaPassageiros[integer];
    }

    /**
     * Um comboio temporário simula a viagem e guarda as estações intermédias, desde
     * a estação de partida até ao destino final usando a lógica de navegação já
     * presente no método run
     * Após terminar o trajeto apenas guardamos todas as paragens intermédias do
     * comboio temporário, no comboio real
     */
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

    /**
     * Método usado para simular a viagem do comboio
     * Deteta em que direção é que o comboio anda, por exemplo, se o comboio vai da estação B para a A ou se ele vai da estação A para a B
     * À medida que a simulação corre, vai alterando as estações de partida para a próxima estação estação intermédia, até chegar ao destino final
     * Quando chega à estação de destino, muda o indicador boolean de ter acabado a viagem
     */
    @Override
    public void run() {
        try {
            if (this.estacaoPartida == this.troco.getEstacoes()[1]) {
                System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A partir da estacao "
                        + this.estacaoPartida.getNome() + " as "
                        + this.getHorarioComboio().getHoraPartida().toString());
                Thread.sleep(100);
                this.estacaoPartida.removeComboio(this);
                if (this.getEstacaoParagem().size() != 0) {
                    if (this.estacoesParagem.get(0).getAddComboio()) {
                        this.estacoesParagem.get(0).addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A chegar a estacao "
                                + this.estacoesParagem.get(0).getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A ignorar estacao...");
                    }
                    if (this.count != 0) {
                        this.estacoesParagem.get(0).movePassageirosToEstacao(this);
                    }
                } else {
                    if (this.destinoFinal.getAddComboio()) {
                        this.destinoFinal.addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A chegar a estacao "
                                + this.destinoFinal.getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A ignorar estacao...");
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
                System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A partir da estacao "
                        + this.estacaoPartida.getNome() + " as "
                        + this.getHorarioComboio().getHoraPartida().toString());
                Thread.sleep(100);
                this.estacaoPartida.removeComboio(this);

                if (this.getEstacaoParagem().size() != 0) {
                    if (this.estacoesParagem.get(0).getAddComboio()) {
                        this.estacoesParagem.get(0).addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A chegar a estacao "
                                + this.estacoesParagem.get(0).getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A ignorar estacao...");
                    }
                    if (this.count != 0) {
                        this.estacoesParagem.get(0).movePassageirosToEstacao(this);
                    }
                } else {
                    if (this.destinoFinal.getAddComboio()) {
                        this.destinoFinal.addComboio(this);
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A chegar a estacao "
                                + this.destinoFinal.getNome() + " as "
                                + this.getHorarioComboio().getHoraChegada().toString());
                        Thread.sleep(100);
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A desembarcar passageiros...");
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - A ignorar estacao...");
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
            //System.out.println("[" + Thread.currentThread().getName() + " (" + this.getCount() + "/" + this.MAX_PASSAGEIROS + ")] - Terminou a viagem!");
        } catch (MaxCapacityException e1) {
            System.out.println("[" + this.estacaoPartida.getNome() + "] -  SOBRELOTADA de comboios");
        } catch (InterruptedException | IOException e) {

        }

    }
}