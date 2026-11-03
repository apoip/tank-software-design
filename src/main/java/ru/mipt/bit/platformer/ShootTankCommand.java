package ru.mipt.bit.platformer;

public class ShootTankCommand implements Command {

    private final GameField field;
    private final Tank tank;

    public ShootTankCommand(GameField field, Tank tank) {
        this.field = field;
        this.tank = tank;
    }

    @Override
    public void execute() {
        field.shoot(tank);
    }
}
