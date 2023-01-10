package trabalhopratico.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HorarioConflictSolver implements Runnable{
    // ArrayList com os comboios todos do projeto
    private ArrayList<Comboio> comboios;

    private FileWriter logWriter;

    public HorarioConflictSolver(ArrayList<Comboio> comboios, FileWriter logWriter) {
        this.comboios = comboios;
        this.logWriter = logWriter;
    }
    

    private boolean hasConflicts() {
        for (int i = 0; i < this.comboios.size(); i++) {
            for (int j = i; i < this.comboios.size(); j++) {
                if ((this.comboios.get(i).getDirecaoComboio() == this.comboios.get(j).getDirecaoComboio()) && (this.comboios.get(i).getHorarioComboio().getHoraPartida() == this.comboios.get(j).getHorarioComboio().getHoraPartida())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void run() {
        while (this.hasConflicts()) {
            for (int i = 0; i < this.comboios.size(); i++) {
                for (int j = i; i < this.comboios.size(); j++) {
                    if ((this.comboios.get(i).getDirecaoComboio() == this.comboios.get(j).getDirecaoComboio()) && (this.comboios.get(i).getHorarioComboio().getHoraPartida() == this.comboios.get(j).getHorarioComboio().getHoraPartida())) {
                        try {
                            this.logWriter.write("O " + this.comboios.get(j).getNomeComboio() + "tinha conflito de horário com o " + this.comboios.get(i).getNomeComboio() + " e o seu horário foi alterado");
                        } catch (IOException e) {}
                        this.comboios.get(j).getHorarioComboio().getHoraPartida().plusMinutes(15);
                        this.comboios.get(j).getHorarioComboio().getHoraChegada().plusMinutes(15);
                        break;
                    }
                }
            }
        }
    }
}
