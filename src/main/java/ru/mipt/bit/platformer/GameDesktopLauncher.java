package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE_PATH =
            "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH =
            "images/greenTree.png";
    private static final float TANK_MOVEMENT_DURATION = 0.4f;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private Batch batch;

    private GameField field;
    private GameFieldGraphics fieldGraphics;
    private List<GameObjectGraphics> objectGraphics;
    private InputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();

        Tank tank = new Tank(1, 1, TANK_MOVEMENT_DURATION);
        Tree tree = new Tree(1, 3);

        field = new GameField(
                tank,
                Arrays.asList(tree)
        );
        fieldGraphics = new GameFieldGraphics(batch, LEVEL_PATH);

        objectGraphics = Arrays.asList(
                new TankGraphics(
                        fieldGraphics.getGroundLayer(),
                        tank,
                        TANK_TEXTURE_PATH,
                        Interpolation.smooth
                ),
                new TreeGraphics(
                        fieldGraphics.getGroundLayer(),
                        tree,
                        TREE_TEXTURE_PATH
                )
        );

        inputHandler = new KeyboardInputHandler(
                Gdx.input::isKeyPressed
        );

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

        inputHandler.handle();
        field.update(Gdx.graphics.getDeltaTime());

        fieldGraphics.render();

        batch.begin();

        for (GameObjectGraphics graphics : objectGraphics) {
            graphics.render(batch);
        }

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
        for (GameObjectGraphics graphics : objectGraphics) {
            graphics.dispose();
        }
        fieldGraphics.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);

        new Lwjgl3Application(
                new GameDesktopLauncher(),
                config
        );
    }
}
