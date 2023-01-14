package trabalhopratico.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HorarioConflictSolver implements Runnable {
    // ArrayList com os comboios todos do projeto
    private ArrayList<Comboio> comboios;

    private FileWriter logWriter;

    public HorarioConflictSolver(ArrayList<Comboio> comboios, FileWriter logWriter) {
        this.comboios = comboios;
        this.logWriter = logWriter;
    }

    private boolean hasConflicts() {
        for (int i = 0; i < this.comboios.size(); i++) {
            for (int j = i + 1; j < this.comboios.size(); j++) {
                // System.out.println("Direcao comboio1: " +
                // this.comboios.get(i).getDirecaoComboio().getSentido());
                // System.out.println("Direcao comboio2: " +
                // this.comboios.get(j).getDirecaoComboio().getSentido());
                // System.out.println("Horario comboio1: " +
                // this.comboios.get(i).getHorarioComboio().getHoraPartida());
                // System.out.println("Horario comboio2: " +
                // this.comboios.get(j).getHorarioComboio().getHoraPartida());
                if ((this.comboios.get(i).getDirecaoComboio().getSentido()
                        .equals(this.comboios.get(j).getDirecaoComboio().getSentido()))
                        && (this.comboios.get(i).getHorarioComboio().getHoraPartida()
                                .equals(this.comboios.get(j).getHorarioComboio().getHoraPartida()))) {
                    if (!this.comboios.get(i).getHasFinished()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void run() {
        while (this.hasConflicts()) {
            for (int i = 0; i < this.comboios.size(); i++) {
                for (int j = i + 1; j < this.comboios.size(); j++) {
                    if ((this.comboios.get(i).getDirecaoComboio().getSentido()
                            .equals(this.comboios.get(j).getDirecaoComboio().getSentido()))
                            && (this.comboios.get(i).getHorarioComboio().getHoraPartida()
                                    .equals(this.comboios.get(j).getHorarioComboio().getHoraPartida()))) {
                        if (!this.comboios.get(i).getHasFinished()) {
                            if (this.comboios.get(i).getNumberOfDelays() < this.comboios.get(j).getNumberOfDelays()) {
                                try {
                                    this.logWriter.write("[Horário Conflict Solver] - O "
                                            + this.comboios.get(i).getNomeComboio()
                                            + " tinha conflito de horário com o "
                                            + this.comboios.get(j).getNomeComboio()
                                            + " e o seu horário foi alterado.\n");
                                    this.logWriter.flush();
                                } catch (IOException e) {
                                }
                            //   System.out.println("Direcao " + this.comboios.get(i).getNomeComboio() + ": " + this.comboios.get(i).getDirecaoComboio().getSentido());
                            //    System.out.println("Direcao " + this.comboios.get(j).getNomeComboio() + ": " + this.comboios.get(j).getDirecaoComboio().getSentido());
                            //    System.out.println("Horario " + this.comboios.get(j).getNomeComboio() + ": " + this.comboios.get(i).getHorarioComboio().getHoraPartida());
                            //    System.out.println("Horario " + this.comboios.get(j).getNomeComboio() + ": " + this.comboios.get(j).getHorarioComboio().getHoraPartida());

                                this.comboios.get(i).getHorarioComboio().setHoraPartida(
                                        this.comboios.get(i).getHorarioComboio().getHoraPartida().plusMinutes(30));
                                this.comboios.get(i).getHorarioComboio().setHoraChegada(
                                        this.comboios.get(i).getHorarioComboio().getHoraChegada().plusMinutes(30));
                                this.comboios.get(i).incrementNumberOfDelays();
                                break;
                            } else {
                                try {
                                    this.logWriter.write("[Horário Conflict Solver] - O "
                                            + this.comboios.get(j).getNomeComboio()
                                            + " tinha conflito de horário com o "
                                            + this.comboios.get(i).getNomeComboio()
                                            + " e o seu horário foi alterado.\n");
                                    this.logWriter.flush();
                                } catch (IOException e) {
                                }
                            /*    System.out.println("Direcao comboio1: " +
                                        this.comboios.get(i).getDirecaoComboio().getSentido());
                                System.out.println("Direcao comboio2: " +
                                        this.comboios.get(j).getDirecaoComboio().getSentido());
                                System.out.println("Horario comboio1: " +
                                        this.comboios.get(i).getHorarioComboio().getHoraPartida());
                                System.out.println("Horario comboio2: " +
                                        this.comboios.get(j).getHorarioComboio().getHoraPartida());
*/
                                this.comboios.get(j).getHorarioComboio().setHoraPartida(
                                        this.comboios.get(j).getHorarioComboio().getHoraPartida().plusMinutes(30));
                                this.comboios.get(j).getHorarioComboio().setHoraChegada(
                                        this.comboios.get(j).getHorarioComboio().getHoraChegada().plusMinutes(30));
                                this.comboios.get(j).incrementNumberOfDelays();
                                break;
                            }

                        }
                    }
                }
            }
        }
    }
}
