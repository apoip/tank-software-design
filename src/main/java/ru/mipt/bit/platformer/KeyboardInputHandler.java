package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

public class KeyboardInputHandler implements InputHandler {

    private final List<KeyBinding> bindings = new ArrayList<>();
    private final IntPredicate isKeyPressed;

    public KeyboardInputHandler(IntPredicate isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    @Override
    public void register(Runnable action, int... keyCodes) {
        bindings.add(new KeyBinding(action, keyCodes));
    }

    @Override
    public void handle() {
        for (KeyBinding binding : bindings) {
            if (binding.isPressed(isKeyPressed)) {
                binding.run();
            }
        }
    }

    private static class KeyBinding {

        private final Runnable action;
        private final int[] keyCodes;

        private KeyBinding(Runnable action, int[] keyCodes) {
            this.action = action;
            this.keyCodes = Arrays.copyOf(
                    keyCodes,
                    keyCodes.length
            );
        }

        private boolean isPressed(IntPredicate isKeyPressed) {
            for (int keyCode : keyCodes) {
                if (isKeyPressed.test(keyCode)) {
                    return true;
                }
            }

            return false;
        }

        private void run() {
            action.run();
        }
    }
}
