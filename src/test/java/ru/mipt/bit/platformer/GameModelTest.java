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
}
