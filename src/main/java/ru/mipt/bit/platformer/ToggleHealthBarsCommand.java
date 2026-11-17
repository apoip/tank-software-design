package ru.mipt.bit.platformer;

/**
 * Application use case for toggling health-bar visibility.
 */
public class ToggleHealthBarsCommand implements Command {

    private final HealthBarVisibility visibility;

    public ToggleHealthBarsCommand(HealthBarVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
