package trabalhopratico.Classes;

import java.time.LocalTime;

public class Horario {
    // hora de partida do comboio
    private LocalTime horaPartida;
    // hora de chegada do comboio
    private LocalTime horaChegada;

    //Construtor do horário
    public Horario(LocalTime horaPartida, LocalTime horaChegada) {
        this.horaPartida = horaPartida;
        this.horaChegada = horaChegada;
    }

    // getter da hora de partida
    /**
     * 
     * @return hora de partida
     */
    public LocalTime getHoraPartida() {
        return this.horaPartida;
    }
    // setter da hora de partida
    public void setHoraPartida(LocalTime horaPartida) {
        this.horaPartida = horaPartida;
    }
    // getter da hora de chegada
    public LocalTime getHoraChegada() {
        return this.horaChegada;
    }
    // setter da hora de chegada
    public void setHoraChegada(LocalTime horaChegada) {
        this.horaChegada = horaChegada;
    }
}
