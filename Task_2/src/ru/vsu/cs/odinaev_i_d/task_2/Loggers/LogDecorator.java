package ru.vsu.cs.odinaev_i_d.task_2.Loggers;

import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Level;
import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Message;

public abstract class LogDecorator extends Logger {
    protected Logger logger;


    public LogDecorator(Logger logger) {
        super(logger.LEVEL);
        this.logger = logger;
    }

    public LogDecorator(Level level) {
        super(level);
        logger.LEVEL = level;
    }

    @Override
    public void log(Message message) {
        logger.log(message);
//        super.log(message);
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
