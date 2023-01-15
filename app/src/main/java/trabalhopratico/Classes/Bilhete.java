package trabalhopratico.Classes;

import java.time.LocalTime;
import java.util.Iterator;

public class Bilhete {
    private Horario validade;
    private Estacao partida;
    private Estacao destino;

    public Bilhete(LocalTime validadePartida, LocalTime validadeChegada, Estacao partida, Estacao destino) {
        this.validade = new Horario(validadePartida, validadeChegada);
        this.partida = partida;
        this.destino = destino;
    }

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

    public Estacao getEstacaoPartida() {
        return this.partida;
    }

    public Estacao getEstacaoDestino() {
        return this.destino;
    }

    public Horario getHorario() {
        return this.validade;
    }
}
