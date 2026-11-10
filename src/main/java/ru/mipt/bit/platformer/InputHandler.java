package ru.mipt.bit.platformer;

public interface InputHandler {

    void register(Runnable action, int... keyCodes);

    void handle();
}
