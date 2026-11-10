package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameModelTest {

    @Test
    public void directionContainsVectorAndRotation() {
        assertEquals(
                new GridPoint2(2, 4),
                Direction.UP.destinationFrom(
                        new GridPoint2(2, 3)
                )
        );
        assertEquals(90f, Direction.UP.getRotation(), 0f);
        assertEquals(
                new GridPoint2(-1, 0),
                Direction.LEFT.getVector()
        );
    }

    @Test
    public void treeOccupiesItsCoordinates() {
        Tree tree = new Tree(1, 3);

        assertTrue(tree.occupies(new GridPoint2(1, 3)));
        assertFalse(tree.occupies(new GridPoint2(2, 3)));
    }

    @Test
    public void tankUsesConfiguredMovementDuration() {
        Tank tank = new Tank(1, 1, 0.8f);

        tank.turn(Direction.RIGHT);
        tank.startMoving(new GridPoint2(2, 1));
        tank.update(0.4f);

        assertTrue(tank.isMoving());
        assertEquals(
                new GridPoint2(1, 1),
                tank.getCoordinates()
        );

        tank.update(0.4f);

        assertFalse(tank.isMoving());
        assertEquals(
                new GridPoint2(2, 1),
                tank.getCoordinates()
        );
    }

    @Test
    public void tankStartsWithOneHundredHealth() {
        Tank tank = new Tank(1, 1, 0.4f);

        assertEquals(100, tank.getHealth());
        assertEquals(100, tank.getMaxHealth());
    }

    @Test
    public void fieldDoesNotMoveTankIntoObstacle() {
        Tank tank = new Tank(1, 1, 0.4f);
        Obstacle obstacle = coordinates -> coordinates.equals(
                new GridPoint2(1, 2)
        );
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.singletonList(obstacle),
                3,
                3
        );

        field.moveTank(tank, Direction.UP);

        assertFalse(tank.isMoving());
        assertEquals(90f, tank.getRotation(), 0f);
    }

    @Test
    public void fieldDoesNotMoveTankOutsideLevel() {
        Tank tank = new Tank(0, 0, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                3,
                3
        );

        field.moveTank(tank, Direction.LEFT);

        assertFalse(tank.isMoving());
        assertEquals(
                new GridPoint2(0, 0),
                tank.getCoordinates()
        );
    }

    @Test
    public void movingTankOccupiesStartAndDestinationCells() {
        Tank movingTank = new Tank(1, 1, 0.4f);
        Tank leftTank = new Tank(0, 1, 0.4f);
        Tank rightTank = new Tank(3, 1, 0.4f);
        List<Tank> tanks = Arrays.asList(
                movingTank,
                leftTank,
                rightTank
        );
        GameField field = createField(
                tanks,
                Collections.emptyList(),
                4,
                3
        );

        field.moveTank(movingTank, Direction.RIGHT);
        field.moveTank(leftTank, Direction.RIGHT);
        field.moveTank(rightTank, Direction.LEFT);

        assertTrue(movingTank.isMoving());
        assertFalse(leftTank.isMoving());
        assertFalse(rightTank.isMoving());
    }

    @Test
    public void fieldUpdatesEveryTank() {
        Tank firstTank = new Tank(0, 0, 0.4f);
        Tank secondTank = new Tank(2, 0, 0.4f);
        GameField field = createField(
                Arrays.asList(firstTank, secondTank),
                Collections.emptyList(),
                3,
                2
        );

        field.moveTank(firstTank, Direction.UP);
        field.moveTank(secondTank, Direction.UP);
        field.update(0.4f);

        assertEquals(
                new GridPoint2(0, 1),
                firstTank.getCoordinates()
        );
        assertEquals(
                new GridPoint2(2, 1),
                secondTank.getCoordinates()
        );
    }

    @Test
    public void moveCommandMovesSelectedTank() {
        Tank tank = new Tank(1, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                3,
                3
        );
        Command command = new MoveTankCommand(
                field,
                tank,
                Direction.RIGHT
        );

        command.execute();

        assertTrue(tank.isMoving());
        assertEquals(
                new GridPoint2(2, 1),
                tank.getDestinationCoordinates()
        );
    }

    @Test
    public void inputHandlerRunsRegisteredActionOnce() {
        AtomicInteger calls = new AtomicInteger();
        InputHandler handler = new KeyboardInputHandler(
                key -> key == 10 || key == 20
        );

        handler.register(calls::incrementAndGet, 10, 20);
        handler.handle();

        assertEquals(1, calls.get());
    }

    @Test
    public void keyboardStrategyExecutesRegisteredCommand() {
        Tank tank = new Tank(1, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                3,
                3
        );
        KeyboardTankControlStrategy strategy =
                new KeyboardTankControlStrategy(
                        new KeyboardInputHandler(key -> key == 10)
                );
        strategy.register(
                new MoveTankCommand(
                        field,
                        tank,
                        Direction.UP
                ),
                10
        );

        strategy.handle();

        assertTrue(tank.isMoving());
        assertEquals(
                new GridPoint2(1, 2),
                tank.getDestinationCoordinates()
        );
    }

    @Test
    public void randomStrategyChoosesDirectionForIdleTank() {
        Tank tank = new Tank(1, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                3,
                3
        );
        TankControlStrategy strategy =
                new RandomTankControlStrategy(
                        field,
                        tank,
                        new Random(0)
                );

        strategy.handle();
        GridPoint2 destination = tank.getDestinationCoordinates();
        strategy.handle();

        assertTrue(tank.isMoving());
        assertEquals(destination, tank.getDestinationCoordinates());
    }

    @Test
    public void keyboardStrategyCreatesCommandBeforeExecution() {
        Tank tank = new Tank(1, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                3,
                3
        );
        KeyboardTankControlStrategy strategy =
                new KeyboardTankControlStrategy(
                        new KeyboardInputHandler(key -> key == 10)
                );
        strategy.register(
                new MoveTankCommand(
                        field,
                        tank,
                        Direction.UP
                ),
                10
        );

        List<Command> commands = strategy.createCommands();

        assertFalse(tank.isMoving());
        assertEquals(1, commands.size());

        commands.get(0).execute();

        assertTrue(tank.isMoving());
    }

    @Test
    public void shootCommandCreatesBulletInFrontOfTank() {
        Tank tank = new Tank(1, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                5,
                3
        );
        Command command = new ShootTankCommand(field, tank);

        command.execute();

        Bullet bullet = findBullet(field);
        assertEquals(
                new GridPoint2(2, 1),
                bullet.getCoordinates()
        );
        assertEquals(Direction.RIGHT, bullet.getDirection());
    }

    @Test
    public void bulletDamagesTankAndDisappears() {
        Tank shooter = new Tank(0, 1, 0.4f);
        Tank target = new Tank(2, 1, 0.4f);
        GameField field = createField(
                Arrays.asList(shooter, target),
                Collections.emptyList(),
                4,
                3
        );

        field.shoot(shooter);
        field.update(0.12f);

        assertEquals(99, target.getHealth());
        assertFalse(containsBullet(field));
    }

    @Test
    public void adjacentTankReceivesDamageImmediately() {
        Tank shooter = new Tank(1, 1, 0.4f);
        Tank target = new Tank(2, 1, 0.4f);
        GameField field = createField(
                Arrays.asList(shooter, target),
                Collections.emptyList(),
                4,
                3
        );

        field.shoot(shooter);

        assertEquals(99, target.getHealth());
        assertFalse(containsBullet(field));
    }

    @Test
    public void destroyedTankIsRemovedFromField() {
        Tank shooter = new Tank(1, 1, 0.4f);
        Tank target = new Tank(2, 1, 0.4f, 1, 0.5f);
        GameField field = createField(
                Arrays.asList(shooter, target),
                Collections.emptyList(),
                4,
                3
        );

        field.shoot(shooter);

        assertFalse(field.getObjects().contains(target));
    }

    @Test
    public void bulletDisappearsWhenItHitsTree() {
        Tank tank = new Tank(1, 1, 0.4f);
        Tree tree = new Tree(2, 1);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.singletonList(tree),
                4,
                3
        );

        field.shoot(tank);

        assertFalse(containsBullet(field));
        assertTrue(field.getObjects().contains(tree));
    }

    @Test
    public void bulletDisappearsAtLevelBoundary() {
        Tank tank = new Tank(0, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                3,
                3
        );

        field.shoot(tank);
        field.update(0.12f);

        assertFalse(containsBullet(field));
    }

    @Test
    public void collidingBulletsBothDisappear() {
        Tank leftTank = new Tank(0, 1, 0.4f);
        Tank rightTank = new Tank(4, 1, 0.4f);
        rightTank.turn(Direction.LEFT);
        GameField field = createField(
                Arrays.asList(leftTank, rightTank),
                Collections.emptyList(),
                5,
                3
        );

        field.shoot(leftTank);
        field.shoot(rightTank);
        field.update(0.12f);

        assertFalse(containsBullet(field));
        assertEquals(100, leftTank.getHealth());
        assertEquals(100, rightTank.getHealth());
    }

    @Test
    public void tankMustReloadBeforeNextShot() {
        Tank tank = new Tank(0, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                6,
                3
        );

        field.shoot(tank);
        int objectCount = field.getObjects().size();
        field.shoot(tank);

        assertEquals(objectCount, field.getObjects().size());

        field.update(0.5f);
        field.shoot(tank);

        assertTrue(field.getObjects().size() > objectCount);
    }

    @Test
    public void fieldObserverReceivesInitialAddAndRemoveEvents() {
        Tank shooter = new Tank(1, 1, 0.4f);
        Tank target = new Tank(2, 1, 0.4f, 1, 0.5f);
        GameField field = createField(
                Arrays.asList(shooter, target),
                Collections.emptyList(),
                4,
                3
        );
        List<GameObject> added = new java.util.ArrayList<>();
        List<GameObject> removed = new java.util.ArrayList<>();

        field.addObserver(new GameFieldObserver() {
            @Override
            public void objectAdded(GameObject object) {
                added.add(object);
            }

            @Override
            public void objectRemoved(GameObject object) {
                removed.add(object);
            }
        });

        field.shoot(shooter);

        assertTrue(added.contains(shooter));
        assertTrue(added.contains(target));
        assertTrue(removed.contains(target));
        assertEquals(3, added.size());
        assertEquals(2, removed.size());
    }

    @Test
    public void randomStrategyCanChooseShootCommand() {
        Tank tank = new Tank(1, 1, 0.4f);
        GameField field = createField(
                Collections.singletonList(tank),
                Collections.emptyList(),
                5,
                3
        );
        Random random = new Random() {
            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }
        };
        TankControlStrategy strategy =
                new RandomTankControlStrategy(
                        field,
                        tank,
                        random
                );

        strategy.handle();

        assertTrue(containsBullet(field));
    }

    @Test
    public void toggleHealthBarsCommandChangesVisibility() {
        HealthBarVisibility visibility =
                new HealthBarVisibility();
        Command command = new ToggleHealthBarsCommand(visibility);

        assertFalse(visibility.isVisible());

        command.execute();
        assertTrue(visibility.isVisible());

        command.execute();
        assertFalse(visibility.isVisible());
    }

    @Test
    public void fileLevelSourceReadsPlayerTanksAndTrees() {
        LevelSource source = new FileLevelSource(
                "T_A\n" +
                        "_X_\n" +
                        "__T"
        );

        LevelLayout layout = source.createLevel();

        assertEquals(3, layout.getWidth());
        assertEquals(3, layout.getHeight());
        assertEquals(
                new GridPoint2(1, 1),
                layout.getPlayerPosition()
        );
        assertEquals(
                Collections.singletonList(new GridPoint2(2, 2)),
                layout.getTankPositions()
        );
        assertEquals(
                Arrays.asList(
                        new GridPoint2(0, 2),
                        new GridPoint2(2, 0)
                ),
                layout.getTreePositions()
        );
    }

    @Test
    public void randomLevelSourceUsesUniqueCells() {
        LevelSource source = new RandomLevelSource(
                4,
                3,
                4,
                3,
                new Random(42)
        );

        LevelLayout layout = source.createLevel();
        Set<GridPoint2> occupiedCells = new HashSet<>();
        occupiedCells.add(layout.getPlayerPosition());
        occupiedCells.addAll(layout.getTankPositions());
        occupiedCells.addAll(layout.getTreePositions());

        assertEquals(3, layout.getTankPositions().size());
        assertEquals(4, layout.getTreePositions().size());
        assertEquals(8, occupiedCells.size());

        for (GridPoint2 position : occupiedCells) {
            assertInsideField(position, 4, 3);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomLevelRejectsTooManyObjects() {
        new RandomLevelSource(2, 2, 2, 2);
    }

    private GameField createField(
            List<? extends MovableObject> tanks,
            List<? extends Obstacle> obstacles,
            int width,
            int height
    ) {
        return new GameField(tanks, obstacles, width, height);
    }

    private void assertInsideField(
            GridPoint2 position,
            int width,
            int height
    ) {
        assertTrue(position.x >= 0 && position.x < width);
        assertTrue(position.y >= 0 && position.y < height);
    }

    private Bullet findBullet(GameField field) {
        for (GameObject object : field.getObjects()) {
            if (object instanceof Bullet) {
                return (Bullet) object;
            }
        }

        throw new AssertionError("Bullet was not found");
    }

    private boolean containsBullet(GameField field) {
        for (GameObject object : field.getObjects()) {
            if (object instanceof Bullet) {
                return true;
            }
        }

        return false;
    }
}
