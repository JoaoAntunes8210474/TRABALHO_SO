package trabalhopratico.App;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import trabalhopratico.Classes.Bilhete;
import trabalhopratico.Classes.Comboio;
import trabalhopratico.Classes.ConflictLogs;
import trabalhopratico.Classes.Estacao;
import trabalhopratico.Classes.Horario;
import trabalhopratico.Classes.HorarioConflictSolver;
import trabalhopratico.Classes.Linha;
import trabalhopratico.Classes.Passageiro;
import trabalhopratico.Exceptions.MaxCapacityException;

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
                Thread.sleep(1000);

                while (iterator.hasNext()) {
                    Thread thread = iterator.next();
                    if (thread.isAlive()) {
                        for (int i = 0; i < threadsPassageiros.size(); i++) {
                            threadsPassageiros.get(i).sleep(400);
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
        for (int i = 0; i <= 3; i++) {
            Estacao estacao = new Estacao("Estacao " + (char) ascii, moduloGestaoConflitoLog.getLogWriter());
            estacoes.add(estacao);
            ascii++;
        }

        // Create the lines
        Linha linhaAB = new Linha(estacoes.get(0), estacoes.get(1));
        Linha linhaBC = new Linha(estacoes.get(1), estacoes.get(2));
        Linha linhaCD = new Linha(estacoes.get(2), estacoes.get(3));
        // Linha linhaDA = new Linha(estacoes.get(6), estacoes.get(0));

        // Add lines to stations
        try {
            estacoes.get(0).addLinhas(linhaAB);
            estacoes.get(1).addLinhas(linhaBC);
            estacoes.get(1).addLinhas(linhaAB);
            estacoes.get(2).addLinhas(linhaCD);
            estacoes.get(2).addLinhas(linhaBC);
        } catch (MaxCapacityException e) {
            e.printStackTrace();
        }

        // Create the trains
        Comboio comboio1 = new Comboio("Comboio 1", estacoes.get(0), estacoes.get(1), estacoes.get(3),
                LocalTime.of(8, 0), LocalTime.of(8, 30), linhaAB, moduloGestaoConflitoLog.getLogWriter());
        try {
            estacoes.get(0).addComboio(comboio1);
        } catch (MaxCapacityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Comboio comboio2 = new Comboio("Comboio 2", estacoes.get(2), estacoes.get(1),
        // estacoes.get(0),
        // LocalTime.of(8, 15), LocalTime.of(8, 45), linhaBC,
        // moduloGestaoConflitoLog.getLogWriter());

        // Comboio comboio2 = new Comboio("Comboio 2", estacoes.get(1), estacoes.get(0),
        // estacoes.get(0), LocalTime.of(8, 0), LocalTime.of(8, 30),
        // moduloGestaoConflitoLog.getLogWriter());

        // comboios.add(comboio1);
        // comboios.add(comboio2);

        // Create tickets
        Bilhete bilhete1 = new Bilhete(LocalTime.of(8,0), LocalTime.of(8,30), estacoes.get(0), estacoes.get(1));

        // Create passengers
        Random random = new Random();
        int nif = random.nextInt(1000000000 - 100000000) + 100000000; // quero meter de A a B

        for (int i = 0; i <= 100; i++) {
            Passageiro passageiro = new Passageiro("Passageiro " + (char) ascii, nif, bilhete1);
            passageiros.add(passageiro);
            ascii++;
            estacoes.get(0).addPassageiro(passageiro);
            passageiro.setComboioEntrar(comboio1);
        }
        // Fazer ciclo for para dar add a estacao diferentes TESTE TESTE TESTE
        // Fazer overload a carregar passageiros, lista de passageiros TESTE TESTE TESTE

        startModules(moduloGestaoConflitoLog.getLogWriter());

        // Horario moduloSimuladorTrafego = new Horario(LocalTime.of(8, 0),
        // LocalTime.of(11, 0));
        /*
         * while (moduloSimuladorTrafego.getHoraPartida() !=
         * moduloSimuladorTrafego.getHoraChegada()) {
         * for (int i = 0; i < comboios.size(); i++) {
         * if (comboios.get(i).getHorarioComboio().getHoraPartida()
         * .compareTo(moduloSimuladorTrafego.getHoraPartida()) == 0) {
         * moduloEmbarque(comboios.get(i));
         * Thread threadComboio = new Thread(comboios.get(i));
         * threadComboio.start();
         * }
         * }
         * 
         * moduloSimuladorTrafego.getHoraPartida().plusMinutes(5);
         * try {
         * Thread.sleep(200);
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         * 
         * }
         */

        Thread threadPassageiro1 = new Thread(estacoes.get(0).getListaPassageiros().get(0));
        threadPassageiro1.setName(comboio1.getNomeComboio());
        threadPassageiro1.start();
        try {
            threadPassageiro1.join();
        } catch (Exception ex) {
        }

        Thread threadComboio1 = new Thread(comboio1);
        // Thread threadComboio2 = new Thread(comboio2);
        threadComboio1.setName(comboio1.getNomeComboio());
        // threadComboio2.setName(comboio2.getNomeComboio());
        threadComboio1.start();
        // threadComboio2.start();
        try {
            threadComboio1.join();
            // threadComboio2.join();
        } catch (Exception ex) {
        }
        // System.out.println(comboio1.getNomeComboio() + "\n" +
        // comboio1.getEstacaoPartida().getNome() + "\n"
        // + comboio1.getEstacaoChegada().getNome() + "\n"
        // + comboio1.getHorarioComboio().getHoraPartida().toString());
        threadComboio1 = new Thread(comboio1);
        // threadComboio2 = new Thread(comboio2);
        threadComboio1.setName(comboio1.getNomeComboio());
        // threadComboio2.setName(comboio2.getNomeComboio());
        threadComboio1.start();
        // threadComboio2.start();
        try {
            threadComboio1.join();
            // threadComboio2.join();
        } catch (Exception ex) {
        }
        // System.out.println(comboio1.getNomeComboio() + "\n" +
        // comboio1.getEstacaoPartida().getNome() + "\n"
        // + comboio1.getEstacaoChegada().getNome() + "\n"
        // + comboio1.getHorarioComboio().getHoraPartida().toString());
    }
}