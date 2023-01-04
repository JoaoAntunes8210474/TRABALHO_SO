package Main;

import java.util.ArrayList;

import Classes.Comboio;
import Classes.Estacao;

public class Main {
    private static ArrayList<Estacao> estacoes;
    private static ArrayList<Comboio> comboios;
    private static ArrayList<Passageiros> passageiros;
    private static ArrayList<Linha> linhas;

    private void startModules() {

    }

    public static ArrayList<Comboio> getAllComboios() {
        return comboios;
    }

    public static void main(String[] args) {
         // Create the stations
        for (int i=0; i<3; i++) {
            Estacao[] estacoes = new Estacao("Estacao"+i);
        }

        
        Estacao estacao1 = new Estacao("Estacao A");
        Estacao estacao2 = new Estacao("Estacao B");

        // Create the lines
        Linha linha1 = new Linha(estacao1, estacao2);


        // Create the trains
        Comboio comboio1 = new Comboio(estacao1, estacao2, LocalTime.of(8, 0), LocalTime.of(12, 0));
        Comboio comboio2 = new Comboio(estacao2, estacao1, LocalTime.of(9, 0), LocalTime.of(13, 0));

        // Create the threads for each train
        Thread threadComboio1 = new Thread(comboio1);
        Thread threadComboio2 = new Thread(comboio2);

        // Start the threads
        threadComboio1.start();
        threadComboio2.start();
        threadComboio3.start();
        threadComboio4.start();       
    }
}
