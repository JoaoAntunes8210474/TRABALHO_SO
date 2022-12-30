package Classes;

import java.util.Arrays;

public class Estacao {

    /*
     * Número máximo de passageiros na estação
     */
    private static final int MAX_PASSAGEIROS = 1000;

    private Passageiro[] listaPassageiros;
    /*
     * Número de passageiros na estação
     */
    private int count;

    /*
     * Construtor da Estação
     */
    public Estacao() {
        this.listaPassageiros = new Passageiro[MAX_PASSAGEIROS];
        this.count = 0;
    }

    public int getCount() {
        return this.count;
    }

    public Passageiro[] getListaPassageiros() {
        return Arrays.copyOf(this.listaPassageiros, MAX_PASSAGEIROS);
    }

    public void add(Passageiro passageiro) {

        if (this.count == MAX_PASSAGEIROS) {
            Passageiro[] listaTemp = Arrays.copyOf(this.listaPassageiros, (MAX_PASSAGEIROS * 2));
            this.listaPassageiros = listaTemp;
        }

        this.count++;

        this.listaPassageiros[this.count - 1] = passageiro;
    }
}
