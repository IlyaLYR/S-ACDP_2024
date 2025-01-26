package ru.vsu.cs.odinaev_i_d.task_2.Loggers;

import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Message;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger extends LogDecorator {
    String fileName;

    public FileLogger(Logger logger, String fileName) {
        super(logger);
        this.fileName = fileName;
    }

    @Override
    public void log(Message message) {
        if (logger.isShow(message.getLevel())) {
//            System.out.println("Логирование в file");
            try {
                FileWriter writer = new FileWriter(fileName, true);
                writer.write(String.format("[%s] -> %s\n", message.getLevel(), message.getText()));
                writer.close();
            } catch (IOException e) {
                System.out.println("Ошибка при записи в файл");
                e.printStackTrace();
            }
        }
        super.log(message);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
