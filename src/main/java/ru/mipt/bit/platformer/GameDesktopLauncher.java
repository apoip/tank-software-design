package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_PATH = "level.tmx";
    private static final String LEVEL_LAYOUT_PATH = "level-layout.txt";
    private static final String TANK_TEXTURE_PATH =
            "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH =
            "images/greenTree.png";
    private static final float TANK_MOVEMENT_DURATION = 0.4f;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;
    private static final int FIELD_WIDTH = 10;
    private static final int FIELD_HEIGHT = 8;
    private static final int RANDOM_TREE_COUNT = 12;

    private final String levelMode;

    private Batch batch;

    private GameField field;
    private GameFieldGraphics fieldGraphics;
    private List<GameObjectGraphics> objectGraphics;
    private InputHandler inputHandler;

    public GameDesktopLauncher() {
        this("file");
    }

    public GameDesktopLauncher(String levelMode) {
        this.levelMode = levelMode;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        LevelLayout layout = createLevelSource().createLevel();
        GridPoint2 playerPosition = layout.getPlayerPosition();

        Tank tank = new Tank(
                playerPosition.x,
                playerPosition.y,
                TANK_MOVEMENT_DURATION
        );

        List<Tree> trees = new ArrayList<>();

        for (GridPoint2 position : layout.getTreePositions()) {
            trees.add(new Tree(position.x, position.y));
        }

        field = new GameField(
                tank,
                trees
        );
        fieldGraphics = new GameFieldGraphics(batch, LEVEL_PATH);

        objectGraphics = new ArrayList<>();
        objectGraphics.add(
                new TankGraphics(
                        fieldGraphics.getGroundLayer(),
                        tank,
                        TANK_TEXTURE_PATH,
                        Interpolation.smooth
                )
        );

        for (Tree tree : trees) {
            objectGraphics.add(
                    new TreeGraphics(
                            fieldGraphics.getGroundLayer(),
                            tree,
                            TREE_TEXTURE_PATH
                    )
            );
        }

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

    private LevelSource createLevelSource() {
        if ("random".equalsIgnoreCase(levelMode)) {
            return new RandomLevelSource(
                    FIELD_WIDTH,
                    FIELD_HEIGHT,
                    RANDOM_TREE_COUNT
            );
        }

        if ("file".equalsIgnoreCase(levelMode)) {
            String levelText = Gdx.files
                    .internal(LEVEL_LAYOUT_PATH)
                    .readString("UTF-8");

            return new FileLevelSource(levelText);
        }

        throw new IllegalArgumentException(
                "Unknown level mode: " + levelMode
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

        String levelMode = args.length == 0 ? "file" : args[0];

        new Lwjgl3Application(
                new GameDesktopLauncher(levelMode),
                config
        );
    }
}
