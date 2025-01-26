package ru.vsu.cs.odinaev_i_d.task_2.Loggers;


import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Level;
import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.Message;

public abstract class Logger {
    protected Level LEVEL;

    public Logger(Level level) {
        this.LEVEL = level;
    }

    public void log(Message message) {
    }

    protected boolean isShow(Level level) {
        switch (level) {
            case INFO:
                return LEVEL == Level.INFO;
            case DEBUG:
                return LEVEL == Level.DEBUG || LEVEL == Level.INFO;
            case ERROR:
                return LEVEL == Level.ERROR || LEVEL == Level.DEBUG || LEVEL == Level.INFO;
            default:
                return false;
        }
    }

    public Level getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(Level LEVEL) {
        this.LEVEL = LEVEL;
    }
}
