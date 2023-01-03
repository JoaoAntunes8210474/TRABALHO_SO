package Classes;

import java.util.ArrayList;

public class HorarioConflictSolver implements Runnable{
    private ArrayList<Comboio> comboios;

    public HorarioConflictSolver(ArrayList<Comboio> comboios) {
        this.comboios = comboios;
    }

    private boolean hasConflicts() {
        


        return false;
    }

    private void solveConflicts() {

    }

    public void run() {
        while (this.hasConflicts()) {
            this.solveConflicts();
        }
    }
}
