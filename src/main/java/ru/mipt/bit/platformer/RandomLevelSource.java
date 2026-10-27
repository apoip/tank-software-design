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
    private final int tankCount;
    private final Random random;

    public RandomLevelSource(int width, int height, int treeCount) {
        this(width, height, treeCount, 0);
    }

    public RandomLevelSource(
            int width,
            int height,
            int treeCount,
            int tankCount
    ) {
        this(width, height, treeCount, tankCount, new Random());
    }

    RandomLevelSource(
            int width,
            int height,
            int treeCount,
            int tankCount,
            Random random
    ) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Level dimensions must be positive"
            );
        }

        if (treeCount < 0 || tankCount < 0) {
            throw new IllegalArgumentException(
                    "Object counts must not be negative"
            );
        }

        if (treeCount + tankCount >= width * height) {
            throw new IllegalArgumentException(
                    "There must be room for all objects"
            );
        }

        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.tankCount = tankCount;
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
        List<GridPoint2> tankPositions = new ArrayList<>();
        List<GridPoint2> treePositions = new ArrayList<>();

        for (int i = 1; i <= tankCount; i++) {
            tankPositions.add(freeCells.get(i));
        }

        for (int i = tankCount + 1;
             i <= tankCount + treeCount;
             i++) {
            treePositions.add(freeCells.get(i));
        }

        return new LevelLayout(
                width,
                height,
                playerPosition,
                tankPositions,
                treePositions
        );
    }
}
