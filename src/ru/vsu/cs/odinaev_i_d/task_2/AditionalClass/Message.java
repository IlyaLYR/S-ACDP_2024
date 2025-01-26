package ru.vsu.cs.odinaev_i_d.task_2.AditionalClass;

public class Message {
    Level level;
    String text;
    public Message(Level level, String text) {
        this.level = level;
        this.text = text;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
