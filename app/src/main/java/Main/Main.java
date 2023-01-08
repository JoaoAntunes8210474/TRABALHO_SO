package Main;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

import Classes.Comboio;
import Classes.Estacao;
import Classes.Horario;
import Classes.HorarioConflictSolver;
import Classes.Linha;
import Classes.Passageiro;

public class Main {
    private static ArrayList<Estacao> estacoes;
    private static ArrayList<Comboio> comboios;
    private static ArrayList<Passageiro> passageiros;
    private static ArrayList<Linha> linhas;

    public static ArrayList<Comboio> getAllComboios() {
        return comboios;
    }

    private static void moduloEmbarque(Comboio comboio) {
        ArrayList<Thread> threadsPassageiros = new ArrayList<>();

        for (int i = 0; i < comboio.getEstacaoPartida().getListaPassageiros().size(); i++) {
            if ((comboio.getEstacaoPartida().getListaPassageiros().get(i).getBilhete().compareTo(comboio.getEstacaoChegada()) == 0) || (comboio.getEstacaoPartida().getListaPassageiros().get(i).getBilhete().compareTo(comboio.getDestinoFinal()) == 0)) {
                comboio.getEstacaoPartida().getListaPassageiros().get(i).setComboioEntrar(comboio);
                Thread threadPassageiro = new Thread(comboio.getEstacaoPartida().getListaPassageiros().get(i));
                threadPassageiro.start();
                threadsPassageiros.add(threadPassageiro);
            }
        }

        while (!threadsPassageiros.isEmpty()) {
            Iterator<Thread> iterator = threadsPassageiros.iterator();

            while (iterator.hasNext()) {
                Thread thread = iterator.next();
                if (!thread.isAlive()) {
                    iterator.remove();
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void startModules() {
        Thread horarioConflictSolver = new Thread(new HorarioConflictSolver(comboios));
        
        horarioConflictSolver.start();
    }

    public static void main(String[] args) {
        estacoes = new ArrayList<>();
        comboios = new ArrayList<>();
        passageiros = new ArrayList<>();
        linhas = new ArrayList<>();

        int ascii = 'A';

        // Create the stations
        for (int i = 0; i < 3; i++) {
            Estacao estacao = new Estacao("Estacao " + (char) ascii);
            estacoes.add(estacao);
            ascii++;
        }

        // Create the lines
        Linha linhaAB = new Linha(estacoes.get(0), estacoes.get(1));
        Linha linhaBC = new Linha(estacoes.get(2), estacoes.get(3));
        Linha linhaCD = new Linha(estacoes.get(4), estacoes.get(5));
        Linha linhaDA = new Linha(estacoes.get(6), estacoes.get(0));

        // Create the trains
        Comboio comboio1 = new Comboio(estacoes.get(0), estacoes.get(1), LocalTime.of(8, 0), LocalTime.of(8, 30));
        Comboio comboio2 = new Comboio(estacoes.get(1), estacoes.get(0), LocalTime.of(8, 0), LocalTime.of(8, 30));

        startModules();

        Horario moduloSimuladorTrafego = new Horario(LocalTime.of(8, 0), LocalTime.of(11,0));

        while (moduloSimuladorTrafego.getHoraPartida() != moduloSimuladorTrafego.getHoraChegada()) {
            for (int i = 0; i < comboios.size(); i++) {
                if (comboios.get(i).getHorarioComboio().getHoraPartida().compareTo(moduloSimuladorTrafego.getHoraPartida()) == 0) {
                    moduloEmbarque(comboios.get(i));
                    Thread threadComboio = new Thread(comboios.get(i));
                    threadComboio.start();
                }
            }

        }


        // Create the threads for each train
        Thread threadComboio1 = new Thread(comboio1);
        Thread threadComboio2 = new Thread(comboio2);

        // Start the threads
        threadComboio1.start();
        threadComboio2.start();
    }
}
