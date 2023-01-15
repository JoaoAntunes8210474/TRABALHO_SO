package trabalhopratico.Classes;

public class Passageiro implements Runnable {
    // Nome do passageiro
    private String name;
    // Nif do passageiro
    private int nif;
    // Bilhete do passageiro
    private Bilhete bilhete;
    // Comboio que o passageiro tem de entrar para ir para a sua estação de destino
    private Comboio comboioEntrar;

    /**
     * Construtor de passageiro
     * 
     * @param name nome do passageiro
     * @param nif nif do passageiro
     * @param bilhete bilhete do passageiro
     */
    public Passageiro(String name, int nif, Bilhete bilhete) {
        this.name = name;
        this.nif = nif;
        this.bilhete = bilhete;
    }

    /**
     * Retorna o nome do passageiro
     * 
     * @return nome do passageiro
     */
    public String getName() {
        return this.name;
    }

    /**
     * Altera o nome do passageiro para aquele que é enviado por referência
     * @param name - novo nome do passageiro
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o nif do passageiro
     * 
     * @return nif do passageiro
     */
    public int getNif() {
        return this.nif;
    }

    /**
     * Altera o nif do passageiro para aquele que é enviado por referência
     * @param nif - novo nif do passageiro
     */
    public void setNif(int nif) {
        this.nif = nif;
    }

    /**
     * Altera o comboio a entrar do passageiro para aquele que é recebido por referência
     * @param comboioEntrar - novo comboio a entrar do passageiro
     */
    public void setComboioEntrar(Comboio comboioEntrar) {
        this.comboioEntrar = comboioEntrar;
    }

    /**
     * Retorna o bilhete do passageiro
     * 
     * @return bilhete do passageiro
     */
    public Bilhete getBilhete() {
        return this.bilhete;
    }

    /**
     * Comportamento normal do passageiro.
     * O semáforo da estação fica bloqueado quando um passageiro tenta entrar no comboio
     * Ele tenta entrar no comboio guardado na variável "comboioEntrar"
     * Após entrar ou não, se o comboio estiver na capacidade cheia
     * O semáforo da estação é desbloqueado.
     */
    @Override
    public void run() {
        try {
            this.bilhete.getEstacaoPartida().getSemaphore().acquire();
        } catch (InterruptedException e) {
        }
        this.bilhete.getEstacaoPartida().movePassageiroToComboio(this, this.comboioEntrar);

        System.out.println("[" + this.getName() + "] - A embarcar passageiro " + Thread.currentThread().getName());
        this.comboioEntrar.getEstacaoPartida().getSemaphore().release();
    }
}
