package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;

    private GameField field;
    private Tank tank;
    private Tree tree;

    @Override
    public void create() {
        batch = new SpriteBatch();

        field = new GameField(batch);

        tank = new Tank(
                field.getGroundLayer(),
                1,
                1
        );

        tree = new Tree(
                field.getGroundLayer(),
                1,
                3
        );
    }

    @Override
    public void render() {
        // Clear the screen.
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        // Read the keyboard and move the tank.
        for (Direction direction : Direction.values()) {
            if (direction.isPressed()) {
                tank.move(direction, tree);
            }
        }

        tank.update(deltaTime);

        // Draw the field first, then the game objects.
        field.render();

        batch.begin();

        tank.render(batch);
        tree.render(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Do not react to window resizing.
    }

    @Override
    public void pause() {
        // The game does not get paused.
    }

    @Override
    public void resume() {
        // The game does not get paused.
    }

    @Override
    public void dispose() {
        tank.dispose();
        tree.dispose();
        field.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        // Level width: 10 tiles x 128 px.
        // Level height: 8 tiles x 128 px.
        config.setWindowedMode(1280, 1024);

        new Lwjgl3Application(
                new GameDesktopLauncher(),
                config
        );
    }
}