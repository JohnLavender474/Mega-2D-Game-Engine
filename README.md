# Mega 2D Game Engine

## Overview

Welcome to the Mega 2D Game Engine, a Kotlin-based game development library built on the popular LibGDX framework. This
library follows an Entity-Component-System (ECS) design, providing extensibility and flexibility for game development.
Although this library is compatible with a Java-based game, it is HIGHLY recommended that you use Kotlin instead for
your game development. This library is designed with Kotlin in mind foremost. No guarantee is made as to the compatibility
and convenience of this library when used in a Java game project.

## Getting Started

### Installation

Include the 2D Game Engine library in your Kotlin project by adding the following dependency to both the "core" and
"desktop" modules in your root build.gradle file:

```
implementation "com.github.JohnLavender474:2D-Game-Engine:$gameEngineVersion"
```

You can find the latest version of the library by checking the releases tab on the GitHub repository.

Alternatively, you can add the library as a local dependency by copy-pasting the .jar file from build/libs into your
project. You can place the .jar file in a "libs" folder in your core module and add the following line to the root 
build.gradle file under project("core"):

```
api fileTree(dir: 'libs', include: '*.jar')
```

Below is an example build.gradle file taken from the "Megaman Maverick" project:

```
buildscript {
    ext.kotlinVersion = '1.8.10'

    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
        maven { url 'https://jitpack.io' }
        google()
    }
}

allprojects {
    apply plugin: "eclipse"

    version = '1.0'
    ext {
        gameEngineVersion = '1.0'
        appName = "Megaman Maverick"
        gdxVersion = '1.12.1'
        roboVMVersion = '2.3.16'
        box2DLightsVersion = '1.5'
        ashleyVersion = '1.7.4'
        aiVersion = '1.8.2'
        gdxControllersVersion = '2.2.1'
    }

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
        maven { url "https://oss.sonatype.org/content/repositories/releases/" }
        maven { url "https://jitpack.io" }
    }

}

project(":desktop") {
    apply plugin: "kotlin"

    dependencies {
        implementation project(":core")
        implementation "com.github.JohnLavender474:2D-Game-Engine:$gameEngineVersion"
        api "com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion"
        api "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop"
        api "com.badlogicgames.gdx-controllers:gdx-controllers-desktop:$gdxControllersVersion"
        api "com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop"
    }
}

project(":core") {
    apply plugin: "kotlin"
    apply plugin: "java-library"

    dependencies {
        implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
        implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
        api "com.badlogicgames.gdx:gdx:$gdxVersion"
        api "com.badlogicgames.gdx-controllers:gdx-controllers-core:$gdxControllersVersion"
        api "com.badlogicgames.gdx:gdx-freetype:$gdxVersion"
        /* TODO: remove implementation lines for fetching repo from jitpack and 
            uncomment line below to use local .jar instead */
        /* api fileTree(dir: 'libs', include: '*.jar') */
        implementation "com.github.JohnLavender474:2D-Game-Engine:$gameEngineVersion"
        testImplementation "com.badlogicgames.gdx:gdx:$gdxVersion"
        testImplementation "io.kotest:kotest-runner-junit5-jvm:4.6.0"
        testImplementation "io.kotest:kotest-runner-junit5:4.0.2"
        testImplementation "io.kotest:kotest-assertions-core:4.0.2"
        testImplementation "io.kotest:kotest-property:4.0.2"
        testImplementation "io.mockk:mockk:1.13.7"
    }
}
```

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
