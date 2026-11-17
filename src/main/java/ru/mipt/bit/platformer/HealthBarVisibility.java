package ru.mipt.bit.platformer;

/**
 * Application state for health-bar visibility.
 */
public class HealthBarVisibility {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
