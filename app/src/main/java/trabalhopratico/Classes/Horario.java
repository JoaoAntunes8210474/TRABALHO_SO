package trabalhopratico.Classes;

import java.time.LocalTime;

public class Horario {
    /*
     * Hora de partida do comboio
     */
    private LocalTime horaPartida;

    /*
     * Hora de chegada do comboio
     */
    private LocalTime horaChegada;

    /* 
     * Construtor do horário
     */
    public Horario(LocalTime horaPartida, LocalTime horaChegada) {
        this.horaPartida = horaPartida;
        this.horaChegada = horaChegada;
    }

    /*
     * Retorna a hora de partida
     */
    public LocalTime getHoraPartida() {
        return this.horaPartida;
    }
    
    /*
     * Define a hora de partida
     */
    public void setHoraPartida(LocalTime horaPartida) {
        this.horaPartida = horaPartida;
    }

    /*
     * Retorna a hora de chegada
     */
    public LocalTime getHoraChegada() {
        return this.horaChegada;
    }

    /*
     * Define a hora de chegada
     */
    public void setHoraChegada(LocalTime horaChegada) {
        this.horaChegada = horaChegada;
    }
}
