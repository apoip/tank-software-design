package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeGraphics implements Disposable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeGraphics(
            TiledMapTileLayer groundLayer,
            Tree tree
    ) {
        texture = new Texture("images/greenTree.png");
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