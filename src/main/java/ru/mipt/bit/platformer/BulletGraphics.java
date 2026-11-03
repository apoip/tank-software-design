package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class BulletGraphics implements GameObjectGraphics {

    private static final int TEXTURE_SIZE = 18;
    private static final int BULLET_RADIUS = 6;

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;
    private final Bullet bullet;

    public BulletGraphics(
            TiledMapTileLayer groundLayer,
            Bullet bullet
    ) {
        Pixmap pixmap = new Pixmap(
                TEXTURE_SIZE,
                TEXTURE_SIZE,
                Pixmap.Format.RGBA8888
        );
        pixmap.setColor(Color.ORANGE);
        pixmap.fillCircle(
                TEXTURE_SIZE / 2,
                TEXTURE_SIZE / 2,
                BULLET_RADIUS
        );
        texture = new Texture(pixmap);
        pixmap.dispose();

        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
        tileMovement = new TileMovement(
                groundLayer,
                Interpolation.linear
        );
        this.bullet = bullet;
    }

    @Override
    public void render(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                bullet.getCoordinates(),
                bullet.getDestinationCoordinates(),
                bullet.getMovementProgress()
        );

        drawTextureRegionUnscaled(
                batch,
                graphics,
                rectangle,
                bullet.getDirection().getRotation()
        );
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
