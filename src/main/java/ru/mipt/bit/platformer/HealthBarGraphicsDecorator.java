package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

public class HealthBarGraphicsDecorator
        implements GameObjectGraphics {

    private static final float BAR_WIDTH = 52f;
    private static final float BAR_HEIGHT = 7f;
    private static final float BORDER_SIZE = 2f;
    private static final float VERTICAL_OFFSET = 34f;

    private final GameObjectGraphics decoratedGraphics;
    private final TiledMapTileLayer groundLayer;
    private final Tank tank;
    private final HealthBarVisibility visibility;
    private final Texture pixel;

    public HealthBarGraphicsDecorator(
            GameObjectGraphics decoratedGraphics,
            TiledMapTileLayer groundLayer,
            Tank tank,
            HealthBarVisibility visibility
    ) {
        this.decoratedGraphics = decoratedGraphics;
        this.groundLayer = groundLayer;
        this.tank = tank;
        this.visibility = visibility;

        Pixmap pixmap = new Pixmap(
                1,
                1,
                Pixmap.Format.RGBA8888
        );
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void render(Batch batch) {
        decoratedGraphics.render(batch);

        if (!visibility.isVisible()) {
            return;
        }

        float centerX = interpolateCenterX();
        float centerY = interpolateCenterY();
        float x = centerX - BAR_WIDTH / 2f;
        float y = centerY + VERTICAL_OFFSET;
        float healthWidth = BAR_WIDTH
                * tank.getHealth()
                / tank.getMaxHealth();
        Color previousColor = new Color(batch.getColor());

        batch.setColor(Color.BLACK);
        batch.draw(
                pixel,
                x - BORDER_SIZE,
                y - BORDER_SIZE,
                BAR_WIDTH + BORDER_SIZE * 2f,
                BAR_HEIGHT + BORDER_SIZE * 2f
        );
        batch.setColor(Color.RED);
        batch.draw(pixel, x, y, BAR_WIDTH, BAR_HEIGHT);
        batch.setColor(Color.GREEN);
        batch.draw(pixel, x, y, healthWidth, BAR_HEIGHT);
        batch.setColor(previousColor);
    }

    private float interpolateCenterX() {
        GridPoint2 start = tank.getCoordinates();
        GridPoint2 destination = tank.getDestinationCoordinates();
        float progress = Interpolation.smooth.apply(
                tank.getMovementProgress()
        );
        float tileX = start.x
                + (destination.x - start.x) * progress;

        return (tileX + 0.5f) * groundLayer.getTileWidth();
    }

    private float interpolateCenterY() {
        GridPoint2 start = tank.getCoordinates();
        GridPoint2 destination = tank.getDestinationCoordinates();
        float progress = Interpolation.smooth.apply(
                tank.getMovementProgress()
        );
        float tileY = start.y
                + (destination.y - start.y) * progress;

        return (tileY + 0.5f) * groundLayer.getTileHeight();
    }

    @Override
    public void dispose() {
        decoratedGraphics.dispose();
        pixel.dispose();
    }
}
