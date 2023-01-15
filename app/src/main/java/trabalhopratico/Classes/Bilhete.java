package trabalhopratico.Classes;

import java.time.LocalTime;
import java.util.Iterator;

public class Bilhete {
    // Horario utilizado para verificar a validade do bilhete
    private Horario validade;
    // Estação de partida do bilhete do passageiro
    private Estacao partida;
    // Estação de destino do bilhete do passageiro
    private Estacao destino;

    /**
     * Construtor de bilhete
     * 
     * @param validadePartida hora de partida válida do bilhete
     * @param validadeChegada hora de chegada válida do bilhete
     * @param partida estação de partida do bilhete do passageiro
     * @param destino estação de destino do bilhete do passageiro
     */
    public Bilhete(LocalTime validadePartida, LocalTime validadeChegada, Estacao partida, Estacao destino) {
        this.validade = new Horario(validadePartida, validadeChegada);
        this.partida = partida;
        this.destino = destino;
    }

    /**
     * Método que verifica se o bilhete do passageiro é válido ou não para entrar no comboio recebido por referência
     * Retorna "TRUE" se for válido e "FALSE" se não for
     * 
     * @param horarioComboio horário do comboio para verificar se está dentro do intervalo de tempo de validade do bilhete do passageiro
     * @param comboio comboio para verificar se o passageiro realmente deveria estar a entrar naquele comboio
     * @return "TRUE" se o bilhete for válido, "FALSE" se não for
     */
    public boolean isValid(Horario horarioComboio, Comboio comboio) {
        Iterator<Estacao> iterador = comboio.getEstacaoParagem().iterator();

        while (iterador.hasNext()) {
            Estacao tempEstacaoParagem = iterador.next();

            if (this.validade.getHoraPartida().compareTo(horarioComboio.getHoraPartida()) <= 0 && this.validade.getHoraChegada().compareTo(horarioComboio.getHoraPartida()) >= 0) {
                if (this.partida.equals(comboio.getEstacaoPartida()) && (this.destino.equals(tempEstacaoParagem) || this.destino.equals(comboio.getDestinoFinal()))) {
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Retorna a estação de partida do bilhete do passageiro
     * 
     * @return estação de partida do bilhete do passageiro
     */
    public Estacao getEstacaoPartida() {
        return this.partida;
    }

    /**
     * Retorna a estação de destino do bilhete do passageiro
     * 
     * @return estação de destino do bilhete do passageiro
     */
    public Estacao getEstacaoDestino() {
        return this.destino;
    }

    /**
     * Retorna o horário do bilhete
     * 
     * @return horário do bilhete
     */
    public Horario getHorario() {
        return this.validade;
    }
}
