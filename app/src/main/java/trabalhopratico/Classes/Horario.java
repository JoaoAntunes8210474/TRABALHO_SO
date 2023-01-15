package trabalhopratico.Classes;

import java.time.LocalTime;

public class Horario {
    // Hora de partida do comboio
    private LocalTime horaPartida;
    // Hora de chegada do comboio
    private LocalTime horaChegada;

    /**
     * Construtor de horário
     * 
     * @param horaPartida hora de partida
     * @param horaChegada hora de chegada
     */
    public Horario(LocalTime horaPartida, LocalTime horaChegada) {
        this.horaPartida = horaPartida;
        this.horaChegada = horaChegada;
    }

    /**
     * Retorna a hora de partida
     * 
     * @return hora de partida
     */
    public LocalTime getHoraPartida() {
        return this.horaPartida;
    }

    /**
     * Muda a hora de partida para a hora de partida enviada por referência
     * 
     * @param horaPartida hora de partida enviada por referência
     */
    public void setHoraPartida(LocalTime horaPartida) {
        this.horaPartida = horaPartida;
    }

    /**
     * Retorna a hora de chegada
     * 
     * @return hora de chegada
     */
    public LocalTime getHoraChegada() {
        return this.horaChegada;
    }

    /**
     * Muda a hora de chegada para a hora de chegada enviada por referência
     * 
     * @param horaChegada hora de chegada enviada por referência
     */
    public void setHoraChegada(LocalTime horaChegada) {
        this.horaChegada = horaChegada;
    }
}
