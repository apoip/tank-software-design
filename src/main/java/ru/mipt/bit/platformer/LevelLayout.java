package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelLayout {

    private final int width;
    private final int height;
    private final GridPoint2 playerPosition;
    private final List<GridPoint2> tankPositions;
    private final List<GridPoint2> treePositions;

    public LevelLayout(
            int width,
            int height,
            GridPoint2 playerPosition,
            List<GridPoint2> tankPositions,
            List<GridPoint2> treePositions
    ) {
        this.width = width;
        this.height = height;
        this.playerPosition = new GridPoint2(playerPosition);
        this.tankPositions = copyPositions(tankPositions);
        this.treePositions = copyPositions(treePositions);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPoint2 getPlayerPosition() {
        return new GridPoint2(playerPosition);
    }

    public List<GridPoint2> getTankPositions() {
        return Collections.unmodifiableList(
                copyPositions(tankPositions)
        );
    }

    public List<GridPoint2> getTreePositions() {
        return Collections.unmodifiableList(
                copyPositions(treePositions)
        );
    }

    private List<GridPoint2> copyPositions(
            List<GridPoint2> positions
    ) {
        List<GridPoint2> result = new ArrayList<>();

        for (GridPoint2 position : positions) {
            result.add(new GridPoint2(position));
        }

        return result;
    }
}
