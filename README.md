# tank-software-design

By default the application reads the level from
`src/main/resources/level-layout.txt`.

The level uses `X` for the player tank, `A` for AI tanks,
`T` for trees and `_` for empty cells.

To generate a random level with AI tanks, pass `random`
as the application argument:

```bash
./gradlew run --args=random
```

The player tank moves with the arrow keys or WASD and
shoots with Space. AI tanks choose movement or shooting
commands automatically.

Press L to show or hide health bars above all tanks.
Every tank starts with 100 health points.

`ToggleHealthBarsCommand` handles the L-key action.
`HealthBarGraphicsDecorator` adds the health bar to
the existing tank graphics.

Bullets disappear after hitting a tree, a tank, another
bullet or the level boundary. A tank is removed after its
health reaches zero.
