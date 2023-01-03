package Classes;

public class Passageiro implements Runnable{
    /*
     * Nome do passageiro
     */
    private String name;
    /*
     * Nif do passageiro
     */
    private int nif;

    /*
     * Bilhete do passageiro
     */
    private Bilhete bilhete;

    public Passageiro(String name, int nif, Bilhete bilhete) {
        this.name = name;
        this.nif = nif;
        this.bilhete = bilhete;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNif() {
        return this.nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public Bilhete getBilhete() {
        return this.bilhete;
    }

    public void run() {
        Comboio comboioUsadoPeloPassageiro = this.bilhete.getEstacaoPartida().getComboioDestinoPassageiro(this);

        if (comboioUsadoPeloPassageiro != null) {
            this.bilhete.getEstacaoPartida().movePassageiroToComboio(this, comboioUsadoPeloPassageiro);
        }
    }
}
