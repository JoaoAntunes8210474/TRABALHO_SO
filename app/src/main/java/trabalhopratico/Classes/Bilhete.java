package trabalhopratico.Classes;

import java.time.LocalTime;

public class Bilhete {
    private Horario validade;
    private Estacao partida;
    private Estacao destino;

    public Bilhete(LocalTime validadePartida, LocalTime validadeChegada, Estacao partida, Estacao destino) {
        this.validade = new Horario(validadePartida, validadeChegada);
        this.partida = partida;
        this.destino = destino;
    }

    public boolean isValid(Horario horarioComboio, Estacao estacaoPartida, Estacao estacaoChegada, Estacao destinoFinal) {
        if (this.validade.getHoraPartida().compareTo(horarioComboio.getHoraPartida()) <= 0 && this.validade.getHoraChegada().compareTo(horarioComboio.getHoraPartida()) >= 0) {
            if (this.partida.equals(estacaoPartida) && (this.destino.equals(estacaoChegada) || this.destino.equals(destinoFinal))) {
                return true;
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
