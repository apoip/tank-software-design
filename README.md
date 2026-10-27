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
