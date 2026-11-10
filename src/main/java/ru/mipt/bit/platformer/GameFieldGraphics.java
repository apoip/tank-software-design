package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;

import java.util.LinkedHashMap;
import java.util.Map;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameFieldGraphics
        implements Disposable, GameFieldObserver {

    private static final String TANK_TEXTURE_PATH =
            "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH =
            "images/greenTree.png";

    private final TiledMap level;
    private final MapRenderer renderer;
    private final TiledMapTileLayer groundLayer;
    private final Map<GameObject, GameObjectGraphics> graphics;
    private final GameField field;
    private final HealthBarVisibility healthBarVisibility;

    public GameFieldGraphics(
            GameField field,
            Batch batch,
            String levelPath
    ) {
        this.field = field;
        level = new TmxMapLoader().load(levelPath);
        renderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        graphics = new LinkedHashMap<>();
        healthBarVisibility = new HealthBarVisibility();
        field.addObserver(this);
    }

    @Override
    public void objectAdded(GameObject object) {
        GameObjectGraphics objectGraphics = createGraphics(object);

        if (objectGraphics != null) {
            graphics.put(object, objectGraphics);
        }
    }

    @Override
    public void objectRemoved(GameObject object) {
        GameObjectGraphics objectGraphics = graphics.remove(object);

        if (objectGraphics != null) {
            objectGraphics.dispose();
        }
    }

    private GameObjectGraphics createGraphics(GameObject object) {
        if (object instanceof Tank) {
            Tank tank = (Tank) object;

            return new HealthBarGraphicsDecorator(
                    new TankGraphics(
                            groundLayer,
                            tank,
                            TANK_TEXTURE_PATH,
                            Interpolation.smooth
                    ),
                    groundLayer,
                    tank,
                    healthBarVisibility
            );
        }

        if (object instanceof Tree) {
            return new TreeGraphics(
                    groundLayer,
                    (Tree) object,
                    TREE_TEXTURE_PATH
            );
        }

        if (object instanceof Bullet) {
            return new BulletGraphics(
                    groundLayer,
                    (Bullet) object
            );
        }

        return null;
    }

    public void renderLevel() {
        renderer.render();
    }

    public void renderObjects(Batch batch) {
        for (GameObjectGraphics objectGraphics
                : graphics.values()) {
            objectGraphics.render(batch);
        }
    }

    public HealthBarVisibility getHealthBarVisibility() {
        return healthBarVisibility;
    }

    @Override
    public void dispose() {
        field.removeObserver(this);

        for (GameObjectGraphics objectGraphics
                : graphics.values()) {
            objectGraphics.dispose();
        }

        graphics.clear();
        level.dispose();
    }
}
