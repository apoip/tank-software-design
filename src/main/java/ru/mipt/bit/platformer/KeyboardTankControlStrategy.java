package ru.mipt.bit.platformer;

public class KeyboardTankControlStrategy implements TankControlStrategy {

    private final InputHandler inputHandler;

    public KeyboardTankControlStrategy(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void register(Command command, int... keyCodes) {
        inputHandler.register(command::execute, keyCodes);
    }

    @Override
    public void handle() {
        inputHandler.handle();
    }
}
