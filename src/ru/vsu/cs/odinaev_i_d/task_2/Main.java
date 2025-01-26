package ru.vsu.cs.odinaev_i_d.task_2;

import ru.vsu.cs.odinaev_i_d.task_2.Loggers.*;
import ru.vsu.cs.odinaev_i_d.task_2.AditionalClass.*;

import java.awt.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //обертываем cmd->file
        Logger INFORMED = new StdLogger(Level.INFO);
        Logger INFOLogger = new FileLogger(INFORMED, "INFO.txt");

       Logger DEBACLE = new StdLogger(Level.DEBUG);
        Logger DebagLogger = new DataBaseLogger((new FileLogger(DEBACLE, "DEBUG.txt")), "data.base.ru");

        Logger ERROR = new StdLogger(Level.ERROR);
        Logger ERRORLogger = new FileLogger(ERROR, "ERROR.txt");

        Message[] ms = new Message[10];
        for (int i = 0; i < 10; i++) {
            ms[i] = new Message(Level.values()[new Random().nextInt(Level.values().length)], "Сообщение " + i);
        }
        for (Message m : ms) {
            INFOLogger.log(m);
            ERRORLogger.log(m);
            DebagLogger.log(m);
        }
    }
}
