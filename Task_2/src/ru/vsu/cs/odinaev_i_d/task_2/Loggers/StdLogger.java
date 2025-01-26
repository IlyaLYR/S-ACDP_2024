package ru.vsu.cs.odinaev_i_d.task_2.Loggers;

import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Level;
import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Message;
import ru.vsu.cs.odinaev_i_d.task_2.Loggers.Logger;

public class StdLogger extends Logger {

    public StdLogger(Level level) {
        super(level);
    }
    @Override
    public void log(Message message) {
        if (isShow(message.getLevel())) {
            System.out.printf("[%s] -> %s\n", message.getLevel(), message.getText());
        }
//        super.log(message);
    }

}