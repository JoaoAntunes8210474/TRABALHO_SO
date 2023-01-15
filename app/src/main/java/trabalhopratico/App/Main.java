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
    public static boolean doorsClosed;
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
            if ((comboio.getEstacaoPartida().getListaPassageiros().get(i).getBilhete().getEstacaoDestino()
                    .equals(comboio.getEstacaoChegada()))
                    || (comboio.getEstacaoPartida().getListaPassageiros().get(i).getBilhete().getEstacaoDestino()
                            .equals(comboio.getDestinoFinal()))) {
                comboio.getEstacaoPartida().getListaPassageiros().get(i).setComboioEntrar(comboio);
                Thread threadPassageiro = new Thread(comboio.getEstacaoPartida().getListaPassageiros().get(i));
                threadPassageiro.setName(comboio.getEstacaoPartida().getListaPassageiros().get(i).getName());
                threadsPassageiros.add(threadPassageiro);
            }
        }

        for (Thread thread : threadsPassageiros) {
            thread.start();
        }

        System.out.println("As portas do comboio abriram.");
        while (!threadsPassageiros.isEmpty()) {
            try {
                Iterator<Thread> iterator = threadsPassageiros.iterator();
                Thread.sleep(5000);
                System.out.println("As portas do comboio fecharam.");

                while (iterator.hasNext()) {
                    Thread thread = iterator.next();
                    if (thread.isAlive()) {
                        thread.sleep(2000);
                    } else {
                        iterator.remove();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void startModules(FileWriter logWriter) {
        Thread horarioConflictSolver = new Thread(new HorarioConflictSolver(comboios, logWriter));

        horarioConflictSolver.start();
        try {
            horarioConflictSolver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        estacoes = new ArrayList<>();
        comboios = new ArrayList<>();
        passageiros = new ArrayList<>();
        linhas = new ArrayList<>();

        ConflictLogs moduloGestaoConflitoLog = new ConflictLogs();

        int ascii = 'A';

        // Create the stations
        for (int i = 0; i <= 4; i++) {
            Estacao estacao = new Estacao("Estacao " + (char) ascii, moduloGestaoConflitoLog.getLogWriter());
            estacoes.add(estacao);
            ascii++;
        }

        // Create the lines
        Linha linhaAB = new Linha(estacoes.get(0), estacoes.get(1));
        Linha linhaBC = new Linha(estacoes.get(1), estacoes.get(2));
        Linha linhaCD = new Linha(estacoes.get(2), estacoes.get(3));
        Linha linhaDE = new Linha(estacoes.get(3), estacoes.get(4));

        // Add lines to stations
        try {
            estacoes.get(0).addLinhas(linhaAB);
            estacoes.get(1).addLinhas(linhaBC);
            estacoes.get(1).addLinhas(linhaAB);
            estacoes.get(2).addLinhas(linhaCD);
            estacoes.get(2).addLinhas(linhaBC);
            estacoes.get(3).addLinhas(linhaDE);
            estacoes.get(3).addLinhas(linhaCD);
        } catch (MaxCapacityException e) {
            e.printStackTrace();
        }

        // Create the trains
        Comboio comboio1 = new Comboio("Comboio 1", estacoes.get(0), estacoes.get(1), estacoes.get(4),
                LocalTime.of(8, 0), linhaAB, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio2 = new Comboio("Comboio 2", estacoes.get(0), estacoes.get(1),
                estacoes.get(4),
                LocalTime.of(8, 0), linhaAB, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio3 = new Comboio("Comboio 3", estacoes.get(0), estacoes.get(1),
                estacoes.get(4),
                LocalTime.of(8, 30), linhaAB, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio4 = new Comboio("Comboio 4", estacoes.get(0), estacoes.get(1),
                estacoes.get(4),
                LocalTime.of(8, 30), linhaAB, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio5 = new Comboio("Comboio 5", estacoes.get(0), estacoes.get(1),
                estacoes.get(4),
                LocalTime.of(8, 30), linhaDE, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio6 = new Comboio("Comboio 6", estacoes.get(4), estacoes.get(3),
                estacoes.get(0),
                LocalTime.of(8, 0), linhaDE, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio7 = new Comboio("Comboio 7", estacoes.get(4), estacoes.get(3),
                estacoes.get(0),
                LocalTime.of(8, 0), linhaDE, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio8 = new Comboio("Comboio 8", estacoes.get(4), estacoes.get(3),
                estacoes.get(0),
                LocalTime.of(8, 0), linhaDE, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio9 = new Comboio("Comboio 9", estacoes.get(4), estacoes.get(3),
                estacoes.get(0),
                LocalTime.of(8, 30), linhaDE, moduloGestaoConflitoLog.getLogWriter());
        Comboio comboio10 = new Comboio("Comboio 10", estacoes.get(4),
                estacoes.get(3), estacoes.get(0),
                LocalTime.of(8, 30), linhaDE, moduloGestaoConflitoLog.getLogWriter());

        try {
            estacoes.get(0).addComboio(comboio1);
            estacoes.get(0).addComboio(comboio2);
            estacoes.get(0).addComboio(comboio3);
            estacoes.get(0).addComboio(comboio4);
            estacoes.get(0).addComboio(comboio5);
            estacoes.get(4).addComboio(comboio6);
            estacoes.get(4).addComboio(comboio7);
            estacoes.get(4).addComboio(comboio8);
            estacoes.get(4).addComboio(comboio9);
            estacoes.get(4).addComboio(comboio10);

        } catch (MaxCapacityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        comboios.add(comboio1);
        comboios.add(comboio2);
        comboios.add(comboio3);
        comboios.add(comboio4);
        comboios.add(comboio5);
        comboios.add(comboio6);
        comboios.add(comboio7);
        comboios.add(comboio8);
        comboios.add(comboio9);
        comboios.add(comboio10);

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
        Bilhete bilhete1 = new Bilhete(LocalTime.of(8, 0), LocalTime.of(17, 0), estacoes.get(0), estacoes.get(4));
        Bilhete bilhete2 = new Bilhete(LocalTime.of(8, 0), LocalTime.of(17, 0), estacoes.get(4), estacoes.get(0));
        Bilhete bilhete3 = new Bilhete(LocalTime.of(8, 0), LocalTime.of(17, 0), estacoes.get(4), estacoes.get(2));
        Bilhete bilhete4 = new Bilhete(LocalTime.of(8, 0), LocalTime.of(17, 0), estacoes.get(3), estacoes.get(2));

        // Create passengers
        Random random = new Random();
        int nif = random.nextInt(1000000000 - 100000000) + 100000000; // quero meter de A a B

        for (int i = 0; i < 40; i++) {
            Passageiro passageiro1 = new Passageiro("Passageiro " + (char) ascii, nif, bilhete1);
            Passageiro passageiro2 = new Passageiro("Passageiro " + (char) ascii, nif, bilhete2);
            Passageiro passageiro3 = new Passageiro("Passageiro " + (char) ascii, nif, bilhete3);
            Passageiro passageiro4 = new Passageiro("Passageiro " + (char) ascii, nif, bilhete4);
            passageiros.add(passageiro1);
            passageiros.add(passageiro2);
            passageiros.add(passageiro3);
            passageiros.add(passageiro4);
            ascii++;
            estacoes.get(0).addPassageiro(passageiro1);
            // estacoes.get(1).addPassageiro(passageiro2);
            // estacoes.get(4).addPassageiro(passageiro3);
            // estacoes.get(3).addPassageiro(passageiro4);
            /*
             * passageiro1.setComboioEntrar(comboio1);
             * passageiro2.setComboioEntrar(comboio2);
             * passageiro3.setComboioEntrar(comboio3);
             * passageiro4.setComboioEntrar(comboio4);
             */
        }
        // Fazer ciclo for para dar add a estacao diferentes TESTE TESTE TESTE
        // Fazer overload a carregar passageiros, lista de passageiros TESTE TESTE TESTE

        startModules(moduloGestaoConflitoLog.getLogWriter());

        System.out.println("Passageiros em A: " + estacoes.get(0).getListaPassageiros().size());
        System.out.println("Passageiros em B: " + estacoes.get(1).getListaPassageiros().size());
        System.out.println("Passageiros em C: " + estacoes.get(2).getListaPassageiros().size());
        System.out.println("Passageiros em D: " + estacoes.get(3).getListaPassageiros().size());
        System.out.println("Passageiros em E: " + estacoes.get(4).getListaPassageiros().size());

        Horario moduloSimuladorTrafego = new Horario(LocalTime.of(8, 0),
                LocalTime.of(15, 0));

        while (moduloSimuladorTrafego.getHoraPartida() != moduloSimuladorTrafego.getHoraChegada()) {
            Thread threadHorarioConflictSolver = new Thread(
                    new HorarioConflictSolver(Main.getAllComboios(),
                            moduloGestaoConflitoLog.getLogWriter()));
            threadHorarioConflictSolver.start();
            try {
                threadHorarioConflictSolver.join();
            } catch (Exception ex) {
            }

            for (int i = 0; i < comboios.size(); i++) {
                if (comboios.get(i).getHorarioComboio().getHoraPartida()
                        .equals(moduloSimuladorTrafego.getHoraPartida())) {
                    moduloEmbarque(comboios.get(i));
                    if (!comboios.get(i).getHasFinished()) {
                        Thread threadComboio = new Thread(comboios.get(i));
                        threadComboio.setName(comboios.get(i).getNomeComboio());
                        threadComboio.start();
                        /*
                         * try {
                         * threadComboio.join();
                         * } catch (InterruptedException e) {
                         * e.printStackTrace();
                         * }
                         */
                    }
                }
            }

            System.out.println("Hora atual: " + moduloSimuladorTrafego.getHoraPartida());
            moduloSimuladorTrafego.setHoraPartida(moduloSimuladorTrafego.getHoraPartida().plusMinutes(5));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try {
            moduloGestaoConflitoLog.getLogWriter().flush();
            moduloGestaoConflitoLog.getLogWriter().close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Passageiros em A: " + estacoes.get(0).getListaPassageiros().size());
        System.out.println("Passageiros em B: " + estacoes.get(1).getListaPassageiros().size());
        System.out.println("Passageiros em C: " + estacoes.get(2).getListaPassageiros().size());
        System.out.println("Passageiros em D: " + estacoes.get(3).getListaPassageiros().size());
        System.out.println("Passageiros em E: " + estacoes.get(4).getListaPassageiros().size());

        /*
         * System.out.println(linhaAB.getSentido());
         * 
         * Thread threadPassageiro1 = new
         * Thread(estacoes.get(0).getListaPassageiros().get(0));
         * threadPassageiro1 = new Thread(estacoes.get(0).getListaPassageiros().get(1));
         * threadPassageiro1 = new Thread(estacoes.get(0).getListaPassageiros().get(2));
         * threadPassageiro1.setName(comboio1.getNomeComboio());
         * threadPassageiro1.start();
         * try {
         * threadPassageiro1.join();
         * } catch (Exception ex) {
         * }
         * 
         * Thread threadComboio1 = new Thread(comboio1);
         * Thread threadComboio2 = new Thread(comboio2);
         * Thread threadComboio3 = new Thread(comboio3);
         * Thread threadComboio4 = new Thread(comboio4);
         * threadComboio1.setName(comboio1.getNomeComboio());
         * threadComboio2.setName(comboio2.getNomeComboio());
         * threadComboio3.setName(comboio3.getNomeComboio());
         * threadComboio4.setName(comboio4.getNomeComboio());
         * threadComboio1.start();
         * threadComboio2.start();
         * threadComboio3.start();
         * threadComboio4.start();
         * try {
         * threadComboio1.join();
         * threadComboio2.join();
         * threadComboio3.join();
         * threadComboio4.join();
         * } catch (Exception ex) {
         * }
         * 
         * Thread threadHorarioConflictSolver1 = new Thread(
         * new HorarioConflictSolver(Main.getAllComboios(),
         * moduloGestaoConflitoLog.getLogWriter()));
         * threadHorarioConflictSolver1.start();
         * try {
         * threadHorarioConflictSolver1.join();
         * } catch (Exception ex) {
         * }
         * 
         * Thread threadPassageiro2 = new
         * Thread(estacoes.get(0).getListaPassageiros().get(0));
         * threadPassageiro2 = new Thread(estacoes.get(0).getListaPassageiros().get(1));
         * threadPassageiro2 = new Thread(estacoes.get(0).getListaPassageiros().get(2));
         * threadPassageiro2.setName(comboio2.getNomeComboio());
         * threadPassageiro2.start();
         * try {
         * threadPassageiro2.join();
         * } catch (Exception ex) {
         * }
         * 
         * threadComboio1 = new Thread(comboio1);
         * threadComboio2 = new Thread(comboio2);
         * threadComboio3 = new Thread(comboio3);
         * threadComboio4 = new Thread(comboio4);
         * threadComboio1.setName(comboio1.getNomeComboio());
         * threadComboio2.setName(comboio2.getNomeComboio());
         * threadComboio3.setName(comboio3.getNomeComboio());
         * threadComboio4.setName(comboio4.getNomeComboio());
         * threadComboio1.start();
         * threadComboio2.start();
         * threadComboio3.start();
         * threadComboio4.start();
         * try {
         * threadComboio1.join();
         * threadComboio2.join();
         * threadComboio3.join();
         * threadComboio4.join();
         * } catch (Exception ex) {
         * }
         * 
         * threadHorarioConflictSolver1 = new Thread(
         * new HorarioConflictSolver(Main.getAllComboios(),
         * moduloGestaoConflitoLog.getLogWriter()));
         * threadHorarioConflictSolver1.start();
         * try {
         * threadHorarioConflictSolver1.join();
         * } catch (Exception ex) {
         * }
         * 
         * Thread threadPassageiro3 = new
         * Thread(estacoes.get(0).getListaPassageiros().get(0));
         * threadPassageiro3 = new Thread(estacoes.get(0).getListaPassageiros().get(1));
         * threadPassageiro3 = new Thread(estacoes.get(0).getListaPassageiros().get(2));
         * threadPassageiro3.setName(comboio3.getNomeComboio());
         * threadPassageiro3.start();
         * try {
         * threadPassageiro3.join();
         * } catch (Exception ex) {
         * }
         * 
         * threadComboio1 = new Thread(comboio1);
         * threadComboio2 = new Thread(comboio2);
         * threadComboio3 = new Thread(comboio3);
         * threadComboio4 = new Thread(comboio4);
         * threadComboio1.setName(comboio1.getNomeComboio());
         * threadComboio2.setName(comboio2.getNomeComboio());
         * threadComboio3.setName(comboio3.getNomeComboio());
         * threadComboio4.setName(comboio4.getNomeComboio());
         * threadComboio1.start();
         * threadComboio2.start();
         * threadComboio3.start();
         * threadComboio4.start();
         * try {
         * threadComboio1.join();
         * threadComboio2.join();
         * threadComboio3.join();
         * threadComboio4.join();
         * } catch (Exception ex) {
         * }
         * // System.out.println(comboio1.getNomeComboio() + "\n" +
         * // comboio1.getEstacaoPartida().getNome() + "\n"
         * // + comboio1.getEstacaoChegada().getNome() + "\n"
         * // + comboio1.getHorarioComboio().getHoraPartida().toString());
         * try {
         * moduloGestaoConflitoLog.getLogWriter().close();
         * } catch (IOException e) {
         * e.printStackTrace();
         * }
         */
    }
}