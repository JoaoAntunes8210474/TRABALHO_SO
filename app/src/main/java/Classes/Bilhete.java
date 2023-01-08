package Classes;

import java.time.LocalTime;

public class Bilhete implements Comparable{
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

    public Estacao getEstacaoPartida() {
        return this.partida;
    }

    public Estacao getEstacaoDestino() {
        return this.destino;
    }

    public Horario getHorario() {
        return this.validade;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Estacao) {
            Estacao tempEstacao = (Estacao) o;

            if (tempEstacao == this.destino) {
                return 0;
            }
        }
        
        return -1;
    }
}
