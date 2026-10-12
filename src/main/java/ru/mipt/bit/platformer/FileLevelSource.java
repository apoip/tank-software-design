package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileLevelSource implements LevelSource {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final List<String> rows;

    public FileLevelSource(String levelText) {
        this(Arrays.asList(levelText.split("\\r?\\n")));
    }

    public FileLevelSource(List<String> rows) {
        this.rows = new ArrayList<>(rows);
    }

    @Override
    public LevelLayout createLevel() {
        if (rows.isEmpty() || rows.get(0).isEmpty()) {
            throw new IllegalArgumentException("Level must not be empty");
        }

        int width = rows.get(0).length();
        int height = rows.size();

        GridPoint2 playerPosition = null;
        List<GridPoint2> treePositions = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            String row = rows.get(rowIndex);

            if (row.length() != width) {
                throw new IllegalArgumentException(
                        "All level rows must have equal length"
                );
            }

            int y = height - rowIndex - 1;

            for (int x = 0; x < width; x++) {
                char cell = row.charAt(x);

                if (cell == TREE) {
                    treePositions.add(new GridPoint2(x, y));
                } else if (cell == PLAYER) {
                    if (playerPosition != null) {
                        throw new IllegalArgumentException(
                                "Level must contain exactly one player"
                        );
                    }
                    playerPosition = new GridPoint2(x, y);
                } else if (cell != EMPTY) {
                    throw new IllegalArgumentException(
                            "Unknown level symbol: " + cell
                    );
                }
            }
        }

        if (playerPosition == null) {
            throw new IllegalArgumentException(
                    "Level must contain exactly one player"
            );
        }

        return new LevelLayout(playerPosition, treePositions);
    }
}
