package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelSource implements LevelSource {

    private final int width;
    private final int height;
    private final int treeCount;
    private final Random random;

    public RandomLevelSource(int width, int height, int treeCount) {
        this(width, height, treeCount, new Random());
    }

    RandomLevelSource(
            int width,
            int height,
            int treeCount,
            Random random
    ) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Level dimensions must be positive"
            );
        }

        if (treeCount < 0 || treeCount >= width * height) {
            throw new IllegalArgumentException(
                    "There must be room for the player"
            );
        }

        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public LevelLayout createLevel() {
        List<GridPoint2> freeCells = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                freeCells.add(new GridPoint2(x, y));
            }
        }

        Collections.shuffle(freeCells, random);

        GridPoint2 playerPosition = freeCells.get(0);
        List<GridPoint2> treePositions = new ArrayList<>();

        for (int i = 1; i <= treeCount; i++) {
            treePositions.add(freeCells.get(i));
        }

        return new LevelLayout(playerPosition, treePositions);
    }
}
