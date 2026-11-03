package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameField {

    private static final float BULLET_MOVEMENT_DURATION = 0.12f;
    private static final int BULLET_DAMAGE = 1;

    private final List<MovableObject> tanks;
    private final List<Obstacle> obstacles;
    private final List<GameObject> objects;
    private final List<GameFieldObserver> observers;
    private final int width;
    private final int height;

    public GameField(
            Collection<? extends MovableObject> tanks,
            Collection<? extends Obstacle> obstacles,
            int width,
            int height
    ) {
        this.tanks = new ArrayList<>(tanks);
        this.obstacles = new ArrayList<>(obstacles);
        objects = new ArrayList<>();
        observers = new ArrayList<>();
        this.width = width;
        this.height = height;

        for (MovableObject tank : tanks) {
            addInitialObject(tank);
        }

        for (Obstacle obstacle : obstacles) {
            addInitialObject(obstacle);
        }
    }

    private void addInitialObject(Object object) {
        if (object instanceof GameObject
                && !objects.contains(object)) {
            objects.add((GameObject) object);
        }
    }

    public void addObserver(GameFieldObserver observer) {
        if (observers.contains(observer)) {
            return;
        }

        observers.add(observer);

        for (GameObject object : objects) {
            observer.objectAdded(object);
        }
    }

    public void removeObserver(GameFieldObserver observer) {
        observers.remove(observer);
    }

    public List<GameObject> getObjects() {
        return Collections.unmodifiableList(
                new ArrayList<>(objects)
        );
    }

    public void moveTank(
            MovableObject tank,
            Direction direction
    ) {
        if (!tanks.contains(tank) || tank.isMoving()) {
            return;
        }

        if (tank instanceof Tank
                && !((Tank) tank).isAlive()) {
            return;
        }

        tank.turn(direction);

        GridPoint2 destination = direction.destinationFrom(
                tank.getCoordinates()
        );

        if (isInside(destination)
                && !isOccupiedByObstacle(destination)
                && !isOccupiedByTank(destination, tank)) {
            tank.startMoving(destination);
        }
    }

    public void shoot(Tank tank) {
        if (!tanks.contains(tank) || !tank.canShoot()) {
            return;
        }

        tank.reload();

        GridPoint2 start = tank.getDirection().destinationFrom(
                tank.getCoordinates()
        );

        if (!isInside(start)) {
            return;
        }

        Bullet bullet = new Bullet(
                start,
                tank.getDirection(),
                BULLET_MOVEMENT_DURATION,
                BULLET_DAMAGE
        );

        addObject(bullet);

        if (!resolveBulletCollision(bullet)) {
            startBulletStep(bullet);
        }
    }

    void advanceBullet(Bullet bullet, float deltaTime) {
        if (!objects.contains(bullet)) {
            return;
        }

        bullet.advance(deltaTime);

        if (!bullet.isMoving()
                && !resolveBulletCollision(bullet)) {
            startBulletStep(bullet);
        }
    }

    private void startBulletStep(Bullet bullet) {
        GridPoint2 destination = bullet.getDirection()
                .destinationFrom(bullet.getCoordinates());

        if (!isInside(destination)) {
            removeObject(bullet);
            return;
        }

        bullet.startMoving();
    }

    private boolean resolveBulletCollision(Bullet bullet) {
        GridPoint2 coordinates = bullet.getCoordinates();
        MovableObject tank = findTankAt(coordinates);

        if (tank != null) {
            removeObject(bullet);

            if (tank instanceof Tank) {
                Tank damagedTank = (Tank) tank;
                damagedTank.takeDamage(bullet.getDamage());

                if (!damagedTank.isAlive()) {
                    removeObject(damagedTank);
                }
            }

            return true;
        }

        if (isOccupiedByObstacle(coordinates)) {
            removeObject(bullet);
            return true;
        }

        Bullet otherBullet = findBulletAt(
                coordinates,
                bullet
        );

        if (otherBullet != null) {
            removeObject(otherBullet);
            removeObject(bullet);
            return true;
        }

        return false;
    }

    private MovableObject findTankAt(GridPoint2 coordinates) {
        for (MovableObject tank : tanks) {
            if (occupies(tank, coordinates)) {
                return tank;
            }
        }

        return null;
    }

    private Bullet findBulletAt(
            GridPoint2 coordinates,
            Bullet currentBullet
    ) {
        for (GameObject object : objects) {
            if (!(object instanceof Bullet)
                    || object == currentBullet) {
                continue;
            }

            Bullet bullet = (Bullet) object;

            if (bullet.getCoordinates().equals(coordinates)
                    || bullet.isMoving()
                    && bullet.getDestinationCoordinates()
                    .equals(coordinates)) {
                return bullet;
            }
        }

        return null;
    }

    private boolean occupies(
            MovableObject tank,
            GridPoint2 coordinates
    ) {
        return tank.getCoordinates().equals(coordinates)
                || tank.isMoving()
                && tank.getDestinationCoordinates()
                .equals(coordinates);
    }

    private boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0
                && coordinates.x < width
                && coordinates.y >= 0
                && coordinates.y < height;
    }

    private boolean isOccupiedByObstacle(GridPoint2 coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return true;
            }
        }

        return false;
    }

    private boolean isOccupiedByTank(
            GridPoint2 coordinates,
            MovableObject movingTank
    ) {
        for (MovableObject tank : tanks) {
            if (tank != movingTank
                    && occupies(tank, coordinates)) {
                return true;
            }
        }

        return false;
    }

    private void addObject(GameObject object) {
        objects.add(object);

        for (GameFieldObserver observer
                : new ArrayList<>(observers)) {
            observer.objectAdded(object);
        }
    }

    private void removeObject(GameObject object) {
        if (!objects.remove(object)) {
            return;
        }

        if (object instanceof MovableObject) {
            tanks.remove(object);
        }

        if (object instanceof Obstacle) {
            obstacles.remove(object);
        }

        for (GameFieldObserver observer
                : new ArrayList<>(observers)) {
            observer.objectRemoved(object);
        }
    }

    public void update(float deltaTime) {
        for (GameObject object
                : new ArrayList<>(objects)) {
            if (objects.contains(object)) {
                object.live(deltaTime, this);
            }
        }
    }
}
