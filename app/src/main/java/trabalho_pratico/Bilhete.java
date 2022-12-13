package trabalho_pratico;

import java.time.LocalTime;

public class Bilhete {
    private Horario validade;
    private Estacao partida;
    private Estacao destino;

    public Bilhete(Estacao partida, Estacao destino) {
        this.validade = new Horario();
        this.partida = partida;
        this.destino = destino;
    }

    public boolean isValid() {
        if () {

            return true;
        }
        
        return false;
    }
}
