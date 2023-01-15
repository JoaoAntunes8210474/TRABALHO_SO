package trabalhopratico.Classes;

public class Passageiro implements Runnable {
    // nome do passageiro
    private String name;
    // nif do passageiro
    private int nif;
    // Bilhete do passageiro
    private Bilhete bilhete;
    // Comboio que o passageiro tem de entrar para ir para a sua estação de destino
    private Comboio comboioEntrar;

    // Construtor
    public Passageiro(String name, int nif, Bilhete bilhete) {
        this.name = name;
        this.nif = nif;
        this.bilhete = bilhete;
    }

    /**
     * 
     * @return nome do passageiro
     */
    public String getName() {
        return this.name;
    }

    /**
     * define o nome do passageiro
     * @param name - recebe uma string como parâmetro
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return nif do passageiro
     */
    public int getNif() {
        return this.nif;
    }

    /**
     * define o nif do passageiro
     * @param nif - recebe um inteiro como parâmetro
     */
    public void setNif(int nif) {
        this.nif = nif;
    }

    /**
     * define o comboio a entrar
     * @param comboioEntrar - recebe um comboio como parâmetro
     */
    public void setComboioEntrar(Comboio comboioEntrar) {
        this.comboioEntrar = comboioEntrar;
    }

    /**
     * 
     * @return bilhete do passageiro
     */
    public Bilhete getBilhete() {
        return this.bilhete;
    }

    /**
     * 
     */
    @Override
    public void run() {
        try {
            this.bilhete.getEstacaoPartida().getSemaphore().acquire();
            Thread.sleep(100); // add this line to slow down the thread
        } catch (InterruptedException e) {
        }
        this.bilhete.getEstacaoPartida().movePassageiroToComboio(this, this.comboioEntrar);

        System.out.println("[" + this.getName() + "] - A embarcar passageiro " + Thread.currentThread().getName());
        this.comboioEntrar.getEstacaoPartida().getSemaphore().release();
    }
}
