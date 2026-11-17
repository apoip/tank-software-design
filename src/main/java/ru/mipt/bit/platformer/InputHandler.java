package ru.mipt.bit.platformer;

/**
 * Application input port for registering and reading user actions.
 */
public interface InputHandler {

    void register(Runnable action, int... keyCodes);

    void handle();
}
