package trabalho_pratico;

import java.util.Arrays;

public class Comboio {
    /*
     * Número máximo de passageiros no comboio
     */
    private static final int MAX_PASSAGEIROS = 200;
    /*
     * Lista de passageiros de um conboio
     */
    private Passageiro[] listaPassageiros;
    /*
     * Número de passageiros no comboio
     */
    private int count;

    public Comboio() {
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
            throw new MaxCapacityException("Comboio cheio!");
        }

        this.count++;

        this.listaPassageiros[this.count - 1] = passageiro;
    }

    
}
