package trabalho_pratico;

public class Linha {
    /*
     * Sentido da linha
     */
    private Estacao sentido;

    /*
     * Número de comboios na linha
     */
    private int nComboios;

    /*
     * Número da linha
     */
    private int nLinha;

    public Linha(Estacao estacaoChegada) {
        this.sentido = estacaoChegada;
        this.nComboios = 0;
        this.nLinha = 0;
    }

    public void setSentido(Estacao sentido){
        this.sentido = sentido;
    }
    public Estacao getSentido(){
        return this.sentido;
    } 

    public void addCombio(){
        this.nComboios++;
    }
    public void subtractComboio(){
        this.nComboios--;
    }

    public void setNDeLinha(int nDeLinha){
        this.nDeLinha = nDeLinha;
    }
    public int getNDeLinha(){
        return this.nDeLinha;
    }
}
