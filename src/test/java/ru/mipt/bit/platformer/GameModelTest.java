package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

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

        assertEquals(
                90f,
                Direction.UP.getRotation(),
                0f
        );

        assertEquals(
                new GridPoint2(-1, 0),
                Direction.LEFT.getVector()
        );
    }

    @Test
    public void treeOccupiesItsCoordinates() {
        Tree tree = new Tree(1, 3);

        assertTrue(
                tree.occupies(new GridPoint2(1, 3))
        );

        assertFalse(
                tree.occupies(new GridPoint2(2, 3))
        );
    }

    @Test
    public void tankCompletesMovement() {
        Tank tank = new Tank(1, 1);

        tank.turn(Direction.RIGHT);
        tank.startMoving(new GridPoint2(2, 1));

        tank.update(0.2f);

        assertTrue(tank.isMoving());
        assertEquals(
                new GridPoint2(1, 1),
                tank.getCoordinates()
        );

        tank.update(0.2f);

        assertFalse(tank.isMoving());
        assertEquals(
                new GridPoint2(2, 1),
                tank.getCoordinates()
        );
    }

    @Test
    public void fieldDoesNotMoveTankIntoTree() {
        Tank tank = new Tank(1, 1);

        GameField field = new GameField(
                tank,
                new Tree(1, 2)
        );

        field.moveTank(Direction.UP);

        assertFalse(tank.isMoving());

        assertEquals(
                new GridPoint2(1, 1),
                tank.getCoordinates()
        );

        assertEquals(
                90f,
                tank.getRotation(),
                0f
        );
    }

    @Test
    public void inputHandlerRunsRegisteredActionOnce() {
        AtomicInteger calls = new AtomicInteger();

        KeyboardInputHandler handler =
                new KeyboardInputHandler();

        handler.register(
                calls::incrementAndGet,
                10,
                20
        );

        handler.handle(
                key -> key == 10 || key == 20
        );

        assertEquals(1, calls.get());
    }
}