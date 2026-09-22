package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;

    private GameField field;
    private GameFieldGraphics fieldGraphics;
    private TankGraphics tankGraphics;
    private TreeGraphics treeGraphics;
    private KeyboardInputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();

        field = new GameField();
        fieldGraphics = new GameFieldGraphics(batch);

        tankGraphics = new TankGraphics(
                fieldGraphics.getGroundLayer()
        );

        treeGraphics = new TreeGraphics(
                fieldGraphics.getGroundLayer(),
                field.getTree()
        );

        inputHandler = new KeyboardInputHandler();

        inputHandler.register(
                () -> field.moveTank(Direction.UP),
                Input.Keys.UP,
                Input.Keys.W
        );

        inputHandler.register(
                () -> field.moveTank(Direction.LEFT),
                Input.Keys.LEFT,
                Input.Keys.A
        );

        inputHandler.register(
                () -> field.moveTank(Direction.DOWN),
                Input.Keys.DOWN,
                Input.Keys.S
        );

        inputHandler.register(
                () -> field.moveTank(Direction.RIGHT),
                Input.Keys.RIGHT,
                Input.Keys.D
        );
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        inputHandler.handle(Gdx.input::isKeyPressed);
        field.update(Gdx.graphics.getDeltaTime());

        fieldGraphics.render();

        batch.begin();

        tankGraphics.render(batch, field.getTank());
        treeGraphics.render(batch);

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
        tankGraphics.dispose();
        treeGraphics.dispose();
        fieldGraphics.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        config.setWindowedMode(1280, 1024);

        new Lwjgl3Application(
                new GameDesktopLauncher(),
                config
        );
    }
}