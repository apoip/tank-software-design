package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank implements Disposable {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(TiledMapTileLayer groundLayer, int x, int y) {
        texture = new Texture("images/tank_blue.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);

        tileMovement = new TileMovement(
                groundLayer,
                Interpolation.smooth
        );

        coordinates = new GridPoint2(x, y);
        destinationCoordinates = new GridPoint2(coordinates);
    }

    public void move(Direction direction, Tree tree) {
        if (!isEqual(movementProgress, 1f)) {
            return;
        }

        GridPoint2 destination =
                direction.destinationFrom(coordinates);

        if (!tree.occupies(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }

        rotation = direction.getRotation();
    }

    public void update(float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                coordinates,
                destinationCoordinates,
                movementProgress
        );

        movementProgress = continueProgress(
                movementProgress,
                deltaTime,
                MOVEMENT_SPEED
        );

        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(
                batch,
                graphics,
                rectangle,
                rotation
        );
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}