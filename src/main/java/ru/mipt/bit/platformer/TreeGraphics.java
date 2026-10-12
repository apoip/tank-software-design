package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeGraphics implements GameObjectGraphics {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeGraphics(
            TiledMapTileLayer groundLayer,
            PositionedObject tree,
            String texturePath
    ) {
        texture = new Texture(texturePath);
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);

        moveRectangleAtTileCenter(
                groundLayer,
                rectangle,
                tree.getCoordinates()
        );
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
