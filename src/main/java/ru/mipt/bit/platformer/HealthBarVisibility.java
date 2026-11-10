package ru.mipt.bit.platformer;

public class HealthBarVisibility {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
