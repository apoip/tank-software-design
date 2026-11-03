package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public interface GameObjectGraphics extends Disposable {

    void render(Batch batch);
}
