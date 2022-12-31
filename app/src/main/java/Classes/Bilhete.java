package Classes;

import java.time.LocalTime;

public class Bilhete {
    private Horario validade;
    private Estacao partida;
    private Estacao destino;

    public Bilhete(Estacao partida, Estacao destino) {
        this.validade = new Horario(LocalTime.of(8, 30), LocalTime.of(19, 15));
        this.partida = partida;
        this.destino = destino;
    }

    public boolean isValid(Horario horarioComboio, Estacao estacaoPartida, Estacao estacaoDestino) {
        if (this.validade.getHoraPartida().compareTo(horarioComboio.getHoraPartida()) < 0 && this.validade.getHoraChegada().compareTo(horarioComboio.getHoraPartida()) >= 0) {
            if (this.partida == estacaoPartida && this.destino == estacaoDestino) {
                return true;
            }
        }
        
        return false;
    }
}
