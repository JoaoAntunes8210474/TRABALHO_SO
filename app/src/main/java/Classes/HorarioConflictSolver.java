package Classes;

import java.util.ArrayList;

public class HorarioConflictSolver implements Runnable{
    private ArrayList<Comboio> comboios;
    private Horario horario;

    public HorarioConflictSolver(ArrayList<Comboio> comboios) {
        this.comboios = comboios;
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
                        this.comboios.get(j).getHorarioComboio().getHoraPartida().plusMinutes(15);
                        this.comboios.get(j).getHorarioComboio().getHoraChegada().plusMinutes(15);
                        break;
                    }
                }
            }
        }
    }
}
