package trabalhopratico.Classes;

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

    /**
     * Comboio que o passageiro tem de entrar para ir para a sua estação de destino
     */
    private Comboio comboioEntrar;

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

    public void setComboioEntrar(Comboio comboioEntrar) {
        this.comboioEntrar = comboioEntrar;
    }

    public Bilhete getBilhete() {
        return this.bilhete;
    }

    public void run() {
        try {
            this.bilhete.getEstacaoPartida().getSemaphore().acquire();
        } catch (InterruptedException e) {
        }
        this.bilhete.getEstacaoPartida().movePassageiroToComboio(this, this.comboioEntrar);

        System.out.println("["+ this.getName() + "] - A embarcar passageiro " + Thread.currentThread().getName());
        this.comboioEntrar.getEstacaoPartida().getSemaphore().release();
    }
}
