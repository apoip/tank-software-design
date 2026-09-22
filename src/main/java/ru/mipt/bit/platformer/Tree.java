package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tree implements Disposable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final GridPoint2 coordinates;
    private final Rectangle rectangle;

    public Tree(TiledMapTileLayer groundLayer, int x, int y) {
        texture = new Texture("images/greenTree.png");
        graphics = new TextureRegion(texture);
        coordinates = new GridPoint2(x, y);
        rectangle = createBoundingRectangle(graphics);

        moveRectangleAtTileCenter(
                groundLayer,
                rectangle,
                coordinates
        );
    }

    public boolean occupies(GridPoint2 destination) {
        return coordinates.equals(destination);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(
                batch,
                graphics,
                rectangle,
                0f
        );
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}