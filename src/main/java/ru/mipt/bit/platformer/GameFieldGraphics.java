package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameFieldGraphics implements Disposable {

    private final TiledMap level;
    private final MapRenderer renderer;
    private final TiledMapTileLayer groundLayer;

    public GameFieldGraphics(Batch batch) {
        level = new TmxMapLoader().load("level.tmx");
        renderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public void render() {
        renderer.render();
    }

    @Override
    public void dispose() {
        level.dispose();
    }
}