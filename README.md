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

Tank behaviour depends on its current health:

- above 70%: normal movement and shooting;
- from 15% to 70%: movement takes twice as long;
- below 15%: movement takes three times as long and
  shooting is disabled.

The behaviour is implemented with `TankState` and the
`LightTankState`, `MediumTankState` and `HeavyTankState`
classes.

`ToggleHealthBarsCommand` handles the L-key action.
`HealthBarGraphicsDecorator` adds the health bar to
the existing tank graphics.

Bullets disappear after hitting a tree, a tank, another
bullet or the level boundary. A tank is removed after its
health reaches zero.

## Architecture

The domain contains `GameField`, `Tank`, `Bullet`, `Tree`,
the object interfaces and the tank states. Commands and
control strategies form the application layer. LibGDX
launching, rendering, keyboard input and level loading are
infrastructure concerns.

Ports are marked in Javadoc on `Command`, `LevelSource`,
`InputHandler`, `TankControlStrategy`, `GameFieldObserver`
and `GameObjectGraphics`. Their implementations are marked
as adapters.

`GameObjectGraphics` still depends directly on LibGDX
types. A stricter ports-and-adapters design would add a
rendering port without `Batch` or `Disposable`. The launcher
also reads files and time through `Gdx`; separate asset and
clock ports would remove those infrastructure dependencies
from the composition code.
