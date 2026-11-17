package ru.mipt.bit.platformer;

/**
 * Application output port that supplies a level layout.
 */
public interface LevelSource {

    LevelLayout createLevel();
}
