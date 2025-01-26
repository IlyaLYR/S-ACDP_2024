package ru.vsu.cs.odinaev_i_d.task_2.Loggers;

import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Message;

public class DataBaseLogger extends LogDecorator {
    String URL;
    public DataBaseLogger(Logger logger, String URL) {
        super(logger);
        this.URL = URL;
    }

    @Override
    public void log(Message message) {
        if (logger.isShow(message.getLevel())) {
            System.out.printf("База данных[%s] -> %s \n", message.getLevel(), message.getText());
        }
        super.log(message);
    }
}
