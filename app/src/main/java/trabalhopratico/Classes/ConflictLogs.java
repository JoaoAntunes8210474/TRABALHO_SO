package trabalhopratico.Classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConflictLogs {
    // Escritor de ficheiro
    FileWriter logWriter;

    /**
     * Construtor que atribui um caminho ao escritor de ficheiro para ele saber onde é que tem de escrever os conflitos
     */
    public ConflictLogs () {
        File file = new File("files/logs.txt");
        String path = file.getAbsolutePath();
        try {
            logWriter = new FileWriter(path);
        } catch (IOException e) {
            System.out.println("Não há nenhum ficheiro com o nome que foi dado");
        }
    }

    /**
     * Retorna o escritor de ficheiros para o ficheiro indicado no método construtor
     * 
     * @return escritor de ficheiros para o ficheiro indicado no método construtor
     */
    public FileWriter getLogWriter() {
        return this.logWriter;
    }
}
