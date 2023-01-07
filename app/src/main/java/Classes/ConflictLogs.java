package Classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConflictLogs {
    FileWriter logWriter;

    public ConflictLogs () {
        File file = new File("LogFile/logs.txt");
        try {
            logWriter = new FileWriter(file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Não há nenhum ficheiro com o nome que foi dado");
        }
    }

    public FileWriter getLogWriter() {
        return this.logWriter;
    }
}
