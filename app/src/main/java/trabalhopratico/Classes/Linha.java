package trabalhopratico.Classes;

import java.util.Arrays;

public class Linha {
    // Sentido da linha
    private String sentido;
    // Estações de comboio
    private Estacao[] estacoesAssociadas;

    /**
     * Construtor da linha
     * 
     * @param primeiraEstacao primeira estação associada à linha
     * @param segundaEstacao segunda estação associada à linha
     */
    public Linha(Estacao primeiraEstacao, Estacao segundaEstacao) {
        StringBuilder sb = new StringBuilder("Linha ");
        this.sentido = sb.append(primeiraEstacao.getNome()).append(" - ").append(segundaEstacao.getNome()).toString();
        this.estacoesAssociadas = new Estacao[2];
        this.estacoesAssociadas[0] = primeiraEstacao;
        this.estacoesAssociadas[1] = segundaEstacao;
    }

    /**
     * Retorna o sentido da linha
     * 
     * @return sentido da linha
     */
    public String getSentido() {
        return this.sentido;
    }

    /**
     * Altera o sentido da linha por aquele recebido por referência
     * 
     * @param sentido - recebe uma string como parâmetro
     */
    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    /**
     * Retorna a estação da linha que não é recebida por parâmetro
     * 
     * @param estacao - recebe uma estação como parâmetro
     * @return estação da linha que não é recebida por parâmetro
     */
    public Estacao getEstacaoArrival(Estacao estacao) {
        return this.estacoesAssociadas[findEstacaoArrival(estacao)];
    }

    /**
     * Retorna uma cópia da lista de estações associadas à linha
     * 
     * @return cópia da lista de estações associadas à linha
     */
    public Estacao[] getEstacoes() {
        return Arrays.copyOf(this.estacoesAssociadas, 2);
    }

    /**
     * Compara o nome dos elementos da lista de estacoes com o do parametro
     * introduzido e devolve a sua posicao na lista
     * 
     * @param estacao - estacao que queremos encontrar
     * @return posição na lista ou -1
     */
    public int findEstacao(Estacao estacao) {
        for (int i = 0; i < this.estacoesAssociadas.length; i++) {
            if (this.estacoesAssociadas[i].getNome().equals(estacao.getNome())) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Compara o nome da posicao 0 da lista de estacoes com o nome da estacao
     * introduzida pelo parametro
     * sabendo que a lista tem apenas duas posicoes, ou a comparação é verdadeira e
     * sabemos que a estação de chegada está na posição 1
     * ou é falso e está na posição zero porque os nomes diferem, e portanto a
     * estação de chegada tem o index de 0
     * 
     * @param estacao - estacao cujo destino queremos saber
     * @return 1 or 0
     */
    public int findEstacaoArrival(Estacao estacao) {
        if (this.estacoesAssociadas[0].getNome().equals(estacao.getNome())) {
            return 1;
        }

        return 0;
    }
}