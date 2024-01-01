# 2D Game Engine

## Overview

Welcome to the 2D Game Engine, a Kotlin-based game development library built on the popular LibGDX framework. This
library follows an Entity-Component-System (ECS) design, providing extensibility and flexibility for game development.

## Getting Started

### Installation

Include the 2D Game Engine library in your Kotlin project by adding the following dependency:

```
TODO: implementation("your.library.coordinates:2d-game-engine:version")
```

Currently, this library is not available on any public repositories since
it is still in development. To use this library, you must copy the core-1.0.jar file
under the /core/libs/ directory into your project and add the jar as a dependency.

## Game2D Class

The Game2D class is the core of your game. Extend it to create your custom game class. It initializes the game engine,
asset manager, input system, and screens.

```kotlin
class MyGame : Game2D() {
    // Implement abstract methods and customize as needed
}
```

## Screens

Screens represent different game states. Extend the IScreen interface and add your game logic. Switch between screens
using the setCurrentScreen method.

```kotlin
class MainMenuScreen(game: IGame2D) : IScreen {
    // Implement screen logic
}
```

## Game Systems

Game systems manage specific aspects of your game. For example, the BehaviorsSystem processes behavior components.
Create custom systems by extending the GameSystem class.

```kotlin
class MyCustomSystem : GameSystem(RequiredCustomComponentType::class) {
    // Implement system logic
}
```

## Entities and Components

Entities are game objects, and components are data containers. Implement custom entities and components by extending
GameEntity and IGameComponent respectively.

```kotlin
class CustomEntity(game: IGame2D) : GameEntity(game) {
    // Implement entity logic
}

class CustomComponent : IGameComponent {
    // Implement component logic
}
```

## Example Project

This library is under active development. To see an example of how to use this library, check out my project
"Megaman Maverick" which is also under active development.

<a href="https://github.com/JohnLavender474/Megaman-Maverick">Megaman Maverick</a>