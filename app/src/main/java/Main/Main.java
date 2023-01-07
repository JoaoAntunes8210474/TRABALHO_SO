package Main;

import java.time.LocalTime;
import java.util.ArrayList;

import Classes.Comboio;
import Classes.Estacao;
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

    private void startModules() {

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
        Comboio comboio1 = new Comboio(estacoes.get(0), estacoes.get(1), LocalTime.of(8, 0), LocalTime.of(12, 0));
        Comboio comboio2 = new Comboio(estacoes.get(1), estacoes.get(0), LocalTime.of(9, 0), LocalTime.of(13, 0));

        // Create the threads for each train
        Thread threadComboio1 = new Thread(comboio1);
        Thread threadComboio2 = new Thread(comboio2);

        // Start the threads
        threadComboio1.start();
        threadComboio2.start();
    }
}
