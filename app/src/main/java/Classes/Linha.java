package Classes;

public class Linha {
    /*
     * Sentido da linha
     */
    private Estacao sentido;

    /*
     * Número de comboios na linha
     */
    private static int numeroComboios;

    /*
     * Identificador da linha
     */
    private int idLinhas;

    public Linha(Estacao estacaoChegada) {
        this.sentido = estacaoChegada;
        this.idLinhas = 0;
        numeroComboios = 0;
    }

    public Estacao getSentido() {
        return this.sentido;
    }

    public int getIdLinhas() {
        return this.idLinhas;
    }

    public int getNumeroComboiosLinha() {
        return numeroComboios;
    }

    public void setSentido(Estacao sentido) {
        this.sentido = sentido;
    }

    public void setIdLinhas(int idLinhas) {
        this.idLinhas = idLinhas;
    }

    public void addCombio() {
        numeroComboios++;
    }

    public void removeComboio() {
        numeroComboios--;
    }
}
