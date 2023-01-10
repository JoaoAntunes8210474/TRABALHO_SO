package Main;

import java.io.FileWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

import Classes.Comboio;
import Classes.ConflictLogs;
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
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < comboio.getEstacaoPartida().getListaPassageiros().size(); i++) {
            if ((comboio.getEstacaoPartida().getListaPassageiros().get(i).getBilhete()
                    .compareTo(comboio.getEstacaoChegada()) == 0)
                    || (comboio.getEstacaoPartida().getListaPassageiros().get(i).getBilhete()
                            .compareTo(comboio.getDestinoFinal()) == 0)) {
                comboio.getEstacaoPartida().getListaPassageiros().get(i).setComboioEntrar(comboio);
                Thread threadPassageiro = new Thread(comboio.getEstacaoPartida().getListaPassageiros().get(i));
                threadsPassageiros.add(threadPassageiro);
            }
        }

        for (Thread thread : threadsPassageiros) {
            thread.start();
        }

        while (!threadsPassageiros.isEmpty()) {
            try {
                Iterator<Thread> iterator = threadsPassageiros.iterator();

                while (iterator.hasNext()) {
                    Thread thread = iterator.next();
                    if (thread.isAlive()) {
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        if (elapsedTime >= 1000) {
                            for (int i = 0; i < threadsPassageiros.size(); i++) {
                                threadsPassageiros.get(i).sleep(400);
                            }
                        }
                    } else {
                        iterator.remove();
                    }
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void startModules(FileWriter logWriter) {
        Thread horarioConflictSolver = new Thread(new HorarioConflictSolver(comboios, logWriter));

        horarioConflictSolver.start();
    }

    public static void main(String[] args) {
        estacoes = new ArrayList<>();
        comboios = new ArrayList<>();
        passageiros = new ArrayList<>();
        linhas = new ArrayList<>();

        ConflictLogs moduloGestaoConflitoLog = new ConflictLogs();

        int ascii = 'A';

        // Create the stations
        for (int i = 0; i < 3; i++) {
            Estacao estacao = new Estacao("Estacao " + (char) ascii, moduloGestaoConflitoLog.getLogWriter());
            estacoes.add(estacao);
            ascii++;
        }

        // Create the lines
        Linha linhaAB = new Linha(estacoes.get(0), estacoes.get(1));
        Linha linhaBC = new Linha(estacoes.get(2), estacoes.get(3));
        Linha linhaCD = new Linha(estacoes.get(4), estacoes.get(5));
        Linha linhaDA = new Linha(estacoes.get(6), estacoes.get(0));

        // Create the trains
        Comboio comboio1 = new Comboio("Comboio 1", estacoes.get(0), estacoes.get(1), estacoes.get(1), LocalTime.of(8, 0), LocalTime.of(8, 30), moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio2 = new Comboio("Comboio 2", estacoes.get(1), estacoes.get(0), estacoes.get(0), LocalTime.of(8, 0), LocalTime.of(8, 30), moduloGestaoConflitoLog.getLogWriter());

        comboios.add(comboio1);
        comboios.add(comboio2);

        startModules(moduloGestaoConflitoLog.getLogWriter());

        Horario moduloSimuladorTrafego = new Horario(LocalTime.of(8, 0), LocalTime.of(11, 0));

        while (moduloSimuladorTrafego.getHoraPartida() != moduloSimuladorTrafego.getHoraChegada()) {
            for (int i = 0; i < comboios.size(); i++) {
                if (comboios.get(i).getHorarioComboio().getHoraPartida()
                        .compareTo(moduloSimuladorTrafego.getHoraPartida()) == 0) {
                    moduloEmbarque(comboios.get(i));
                    Thread threadComboio = new Thread(comboios.get(i));
                    threadComboio.start();
                }
            }

            moduloSimuladorTrafego.getHoraPartida().plusMinutes(5);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
