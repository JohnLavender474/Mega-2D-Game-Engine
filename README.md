# Mega 2D Game Engine

## Overview

`Mega 2D Game Engine` is a Kotlin-based game development engine built on the popular LibGDX framework. 
This project is designed to provide very simple yet effective and flexible tools for designing a 2D game in LibGDX. 
This project is designed with Kotlin in mind first and foremost, although there is compatibility with Java. It is 
highly recommended to use this project with a Kotlin-based repository rather than a Java-based one.

To see how to install and use this library in your game, see the [Installation](#Installation) section.

The engine uses the [Entity-Component-System](https://en.wikipedia.org/wiki/Entity_component_system) pattern. Classes
for entities, components, systems, and the engine are provided in this project. To read more on how ECS is implemented
and used in this project, see the [Architecture](#Architecture) section.

To see the engine in action, see [Megaman Maverick](https://github.com/JohnLavender474/Megaman-Maverick)!

## Installation

Include the 2D Game Engine library in your Kotlin project by following these steps:
- Download the latest JAR of the `Mega 2D Game Engine` project from the root directory of the Github repo. It will be titled something like `2D-Game-Engine-[version].jar`.
- Move the downloaded jar file to the following location in your LibGDX project: `core/libs`
- Add the following lines to the `project("core")` section in your `build.gradle` file: `api fileTree(dir: 'libs', include: '*.jar')`
- If you are using IntelliJ (which is highly recommended), you may need to right-click the JAR file and select "Add as library..." in order for the JAR code to be accessible by the IDE

Check out the [build.gradle file in the Megaman Maverick project](https://github.com/JohnLavender474/Megaman-Maverick/blob/master/build.gradle) for an example.

If you are making changes to this project locally and wish to create a new updated JAR, simply run the `./gradlew build` command, and the new JAR will appear under `build/libs`.

## Architecture

Simply put, ECS or Entity-Component-System in the case of this project is an architectural pattern that consists of the 
following parts:
- Components
- Entities
- Systems

### Components

The simplest piece of this architecture are the components. A component is in essence nothing more than a bucket of 
data to be associated with an object in the game. The data could be anything. It could be data for an object's 
position. It could also be an object's physics data such as its velocity, friction, etc. Or, it could be the sprite 
that is drawn to represent the object in the game.

In this project, components are marked by the interface 
[IGameComponent](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/components/IGameComponent.kt).
There is a single method `reset` that can optionally either be implemented by the extending component class or else left
as a no-op method. We'll come back to explain the significance of the `reset` method in more detail. As you can see,
it is a very simple interface. So, how might an implementation of this interface look like? Well, lucky for you, there
are many pre-created components in this project that you can see! Let's look at 
[SpritesComponent](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/drawables/sprites/SpritesComponent.kt)
as an example.

The `SpritesComponent` class implements the `IGameComponent` interface. In the `SpritesComponent` class, there are two
pieces of data: 
- `sprites`: a map of entries where the key of an entry is a `String` and the value is a [GameSprite](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/drawables/sprites/GameSprite.kt) instance (which extends LibGDX's `Sprite` class) that belongs to the game object that owns the component.
- `updatables`: a map of entries where the key of an entry is the `String` key of a sprite in the `sprites` map and the value is an `UpdateFunction` instance. This is used to associate an `UpdateFunction` to a sprite contained in the `sprites` map; the `UpdateFunction` is run once each frame in the game.

The class contains the above data along with methods that can be used to manipulate the data. So, now that we see the data
for sprites, two questions arise: 
- Who "owns" this data?
- How is this data used? 

The first question will be answered in the following [Entities](#Entities) section. The second question will be answered in the following [Systems](#Systems) section.

To create your own component is very simple. Let's say you want to have a component class called `FooComponent` which contains a boolean value `foo`. This
component is used to set or get the value for whether the owning entity is a `foo` or not. For now, we'll implement the `reset` method to set the value of
`foo` to false. We'll come back to what effect this has in the [Engine](#Engine) section.

```kotlin
class FooComponent(var foo: Boolean = false): IGameComponent {    

    override fun reset() {
        foo = false
    }
}
```

You can also implement this in Java if you prefer, though as noted earlier, this project is designed specifically with Kotlin in mind.

```java
public class FooComponent implements IGameComponent {
    
    private boolean foo;
    
    public FooComponent() {
        this(false);
    }
    
    public FooComponent(boolean foo) {
        this.foo = foo;
    }
    
    public void setFoo(boolean foo) {
        this.foo = foo;
    }
    
    public boolean getFoo() {
        return foo;
    } 
    
    @Override
    public void reset() {
        this.foo = false;
    }
}
```

### Entities

In the previous section on components, we've talked about "game objects" owning components, which in turn means that the "game object" owns the data
inside the component. What exactly are these "game objects"? Actually, from now on, we won't refer to these as "game objects" but rather as "Entities".

In ECS, entities are objects that own components. If a component is a bucket to contain data, then an entity is a bucket to contain components.

In this project, entities are defined as objects that (1) contain components and (2) have the following lifecycle:
- init: called when the entity is first instantiated (entity instances should only ever be initialized once)
- spawn: called every time the entity is spawned (entities can be spawned more than once)
- kill: called in order to kill the entity
- on destroy: called when the entity is destroyed (a destroyed entity can be re-spawned)
We'll come back to the lifecycle part more in-depth in the [Engine](#Engine) section. In this section, we'll focus on the relationship between entities and components.

If you take a look at the [IGameEntity](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/entities/IGameEntity.kt)
interface, you'll see that among other methods, there are methods for adding components to an entity, removing components from an entity, and 
getting an entity's owned components. Each of these methods receives a class instance as a parameter. For any game engine, there are a multitude
of ways to implement this functionality. In the case of this project, I decided to use the class itself as the parameter for adding, removing, and
getting components. The reasoning behind this is that, in my opinion, an entity should never have more than one instance of any type of component.
On top of that, component classes should never extend another component class, but rather always implement `IGameComponent` and nothing more. 
If an entity requires more than one instance of a component type, then this (at least in my opinion) signifies a bad design decision with the
component. Yes, this is a very opinionated stance, and the fact that this is reflected strongly in the `IGameEntity` interface means that it is
almost impossible to deviate from this design decision when using this game engine. With this being said, it is up to the programmer to decide
if this is a dealbreaker or not.

The [GameEntity](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/entities/GameEntity.kt) class provides an 
implementation for the `IGameEntity` interface. This is the implementation that I have come to use in my projects, as I find it most effective.
That being said, if the implementation is not satisfactory for your needs, then there is always the option of implementing the `IGameEntity`
interface with your own base implementation instead. In this implementation, components are stored in a map where the component class is the key
and the component itself is the value.

For the sake of simplicity, we'll use the `GameEntity` implementation. Now, let's say you want to instantiate `FooComponent` and add the instance
to the game entity. That might look something like this.

```kotlin
val gameEntity = GameEntity()
val fooComponent = FooComponent(false)
gameEntity.addComponent(fooComponent)
```

To fetch the component from the entity, do the following.

```kotlin
val fooComponent = gameEntity.getComponent(FooComponent::class)
```

Calling the following will overwrite the old `FooComponent` with the new one.

```kotlin
val newFooComponent = FooComponent(true)
gameEntity.addComponent(newFooComponent)

// later on...

val newFooComponentInstance = gameEntity.getComponent(FooComponent::class) // will be reference to 'newFooComponent'
```

So the question might arise: Where in the code should I add components to entities? This can be done in various ways based on your preferred design choices.
Personally, I like for components to be created and added to the entity from within the entity class, as components really should never be owned by any other 
entity besides the one they're originally instantiated for. Of course, this might vary depending on the context, but it's a good rule of thumb regardless.
I've never run into a situation where I wanted a component transferred from one entity to another. In fact, I would argue that doing so is bad practice and
would mean that a different approach should be considered.

If you look in the code for game entities in [Megaman Maverick](https://github.com/JohnLavender474/Megaman-Maverick), you'll see that components are defined
and added to entities from within the `IGameEntity.init` method. As said before, we'll go over the lifecyle of entities in the [Engine](#Engine) section,
but for now, it suffices to know that the `init` method is only called once when an entity is "first instantied in the game engine": this is separate from 
when the instance of the class is created, though it aligns close enough that it suffices to think of it in that term for now. In other words, the `init`
method is (or at least should be) called once and only once for an instance of an entity regardless of how many times it is re-spawned. Apologies if that 
sounds confusing. I promise, we'll go over this in the systems section. Anyways, I digress. We were talking about components being added to entities from 
within the entity's `init` method. Here's what that might look like if you're extending the `GameEntity` class.

```kotlin
class MyGameEntity: GameEntity() {

    // the init method is called only once for an instance of an entity
    // this can be used to "initialize" an entity with its components
    override fun init() {
        addComponent(defineFooComponent)
        addComponent(defineBarComponent)
    }

    // the spawn method is called every time the entity is spawned
    // this should be used to set values for components or other data
    override fun spawn(spawnProps: Properties) {
        // get the spawn prop for "is foo" and set the component's value to it
        val isFoo = spawnProps.getOrDefault("foo", false, Boolean::class)
        val fooComponent = getComponent(FooComponent::class)!!
        fooComponent.foo = isFoo
        
        // do something similar with the bar component here...
    }
    
    // this method is only ever called once when the "init" method is called
    // it's cleaner to move the component declaration into its own private method
    // rather than clutter the "init" method with the implementation details
    private fun defineFooComponent(): FooComponent {
        val fooComponent = FooComponent()
        // do other things here with the component
        return fooComponent
    }
    
    private fun defineBarComponent(): BarComponent {
        val barComponent = BarComponent()
        // do other things here with the component
        return barComponent
    }
}
```

If you look in the directory `src/main/com/engine/entities/contracts`, you'll see interfaces which are designed as convenience interfaces for entities
that use components. For example, [ISpritesEntity](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/entities/contracts/ISpritesEntity.java)
provides convenience methods for a game entity that has the `SpritesComponent` component.

Let's do something similar with our `FooComponent`. First, we'll define our own interface `IFooEntity` which is for entities that have the
`FooComponent` component.

```kotlin
interface IFooEntity: IGameEntity {
   
    // since this interface should only be implemented by an entity that ALWAYS has the foo component, we'll always apply a null check
    // if the foo component is optional for this method, then this interface would need to be refactored to reflect that optionality
    fun getFooComponent(): FooComponent = getComponent(FooComponent::class)!!
    
    fun getFoo(): Boolean = getFooComponent().foo
    
    fun setFoo(foo: Boolean) {
        getFooComponent().foo = foo
    }
}
```

And if our game entity implements this interface, it (1) makes it much clearer that this entity class has the foo component and (2)
makes our code cleaner via the interface's convenience methods.

```kotlin
class MyGameEntity: GameEntity(), IFooEntity {

    component object {
        private const val FOO = "foo"
        private const val DEFAULT_FOO = false
    }
    
    override fun init() {
        addComponent(defineFooComponent())
        addComponent(defineBarComponent())
    }

    override fun spawn(spawnProps: Properties) {       
        val isFoo = spawnProps.getOrDefault(FOO, DEFAULT_FOO, Boolean::class)
        setFoo(isFoo) 
        // we don't have to explicitly fetch the component here since the interface's default implementation already does that for us
        // using this approach makes it a lot cleaner and less cumbersome to work with an entity that has multiple components
        // you can create an interface 'IBarEntity' for the 'BarComponent' and have this entity implement that interface as well
        // this is the magic of interfaces with default implementations!
    }
       
    private fun defineFooComponent(): FooComponent = FooComponent(DEFAULT_FOO)
    
    private fun defineBarComponent(): BarComponent = TODO("Define bar component here")
}
```

### Systems

Now that we've covered entities and components, it's time to discuss systems. To quickly recap, we've seen that components are essentially buckets to contain data
while entities are essentially buckets to contain components. Entities own components which means that the data in the component belongs to the entity. 

So okay, we have the data in our components, and we have the components mapped to our entities, so now what? Now is when systems come into play.

Put simply, systems are designed to manipulate the data of specific components for each entity registered in the system. For example, a physics system
would take and manipulate the physics component of each entity registered in the system. In this project, systems all implement this following interface:
[IGameSystem](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/systems/IGameSystem.kt). In the same package as the
interface is a default implementation titled `GameSystem` along with some special extensions of the system class. 

Game systems run on an update cycle which is controlled by the `GameEngine` class. We'll come back to discussing the update cycle in the [Engine](#Engine) section,
but for now for the sake of simplicity, assume that the `update` method of each system runs once per frame.

Based on the methods for `contains`, `add`, and `remove` methods, it is clear that systems have some sort of reference to game entities. The interface 
does not provide default implementations for these methods since it is up to the programmer to decide the best way to handle this.

Looking at the `GameSystem` implementation (which is my preferred implementation for the interface), we can see that the system takes in a `MutableCollection`
of entities in the constructor. By default, this collection is initialized as an empty set, and in most cases, this is to be preferred, as entities should
be added to the system via the system class's `add` method which checks if the entity "qualifies" to be in the system via the `qualify` method.

What does it mean for an entity to "qualify" to be in a system. Well, remember the example I gave earlier about how a physics system would manipulate the
physic component of entities? Obviously, in order for the system logic to work, each entity in the system would need to have the physics component, but not
all entities in our game have the physics component. Therefore, we should only be adding entities that have the physics component to our physics system. 
Lucky for us, the `GameSystem` class provides the `qualifies` method for us to make this simple. 

The example with the physics component and system is simple since the physics system only requires that its entities have the single physics component. 
However, other systems might require entities to have multiple components. Again, the `qualifies` method checks this for us.

If you look at the constructor for `GameSystem`, you'll see that the first argument is called `componentMask` and is a set of component class types.
This is how we set which component types are required for an entity to "qualify" to be in a system. 

Let's say that we want to implement a `FooSystem` class which changes the value of `foo` in `FooComponent` each frame based on a result retrieved
from `BarComponent`. For simplicity, `BarComponent` will have a boolean variable `bar`, and in the system, we'll set `foo` to the opposite of `bar`.

The process method in the system implementation takes in the following arguments:
- "on": Whether the system is on or off. The "process" method is called regardless of whether the system is "on" or "off"; it is up to the programmer to decide what happens when the system is "on" or "off".
- "entities": An immutable view of the entities contained in this system. The collection is specified as immutable to make it clear that the system's entity collection should NOT be manipulated while processing. We'll touch on this later
- "delta": The delta time since the last frame.

```kotlin
class MyGameEntity: GameEntity(), IFooEntity, IBarEntity {

    component object {
        private const val FOO = "foo"
        private const val BAR = "bar"
        private const val DEFAULT_FOO = false
        private const val DEFAULT_BAR = true
    }
    
    override fun init() {
        addComponent(defineFooComponent())
        addComponent(defineBarComponent())
    }

    override fun spawn(spawnProps: Properties) {       
        val isFoo = spawnProps.getOrDefault(FOO, DEFAULT_FOO, Boolean::class)
        setFoo(isFoo) 
        
        val isBar = spawnProps.getOrDefault(BAR, DEFAULT_BAR, Boolean::class)
        setBar(isBar)
    }
       
    private fun defineFooComponent(): FooComponent = FooComponent(DEFAULT_FOO)
    
    private fun defineBarComponent(): BarComponent = BarComponent(DEFAULT_BAR)
}

// calls the vararg constructor with classes for FooComponent and BarComponent, which means that the system requires its entities to have both components 
class FooSystem: GameSystem(FooComponent::class, BarComponent::class) {

    // on each update cycle, process is called
    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        // if the system is not on, then we'll simply return without doing anything
        // however, if desired, we could do other things before returning if the system if "off", or else we could entirely ignore the concept of "on" or "off" if desired       
        if (!on) return
        
        entities.forEach { entity ->
            val fooComponent = entity.getComponent(FooComponent::class)!!
            val barComponent = entity.getComponent(BarComponent::class)!!
            // set "foo" to the opposite of "bar"
            fooComponent.foo = !barComponent.bar
        }
    }
}

// some method somewhere, doesn't matter where for the sake of simplicity
fun someMethod() {
    val fooSystem = FooSystem()
    
    // entity1 will be added to the system successfully on the call to "fooSystem.add" since it has both of the required components 
    val entity1 = GameEntity()
    entity1.addComponent(FooComponent())
    entity1.addComponent(BarComponent())
    
    // entity2 does not qualify to be added to the system since it's missing the required bar component
    val entity2 = GameEntity()
    entity2.addComponent(FooComponent())
    
    val entity1Added = fooSystem.add(entity1)
    println(entity1Added) // true
    val entity2Added = barSystem.add(entity2)
    println(entity2Added) // false
}
```

### Engine

We've seen the "entity", "component", and "system" parts of ECS in action above. However, we're still missing a crucial part of the game
which ties the three parts all together. This is where the "engine" comes into play. 

How to implement the "engine" is entirely up to opinion. This project provides a [GameEngine](https://github.com/JohnLavender474/Mega-2D-Game-Engine/blob/master/src/main/com/engine/GameEngine.kt)
class which serves the purpose of a game engine. However, this class is optional and can be easily replaced with your own implementation.

Nonetheless, let's dive into the `GameEngine` implementation as it illuminates certain decisions that were made in designing the other parts of the project.

The `GameEngine` class contains (among other things) collections for systems and entities. 

The `GameEngine` class is responsible for handling the spawning of entities. If you look at the `spawn` method in `GameEngine` along with the `spawn` method
in `GameEntity`, you'll see that the `spawn` method in `GameEngine` is called first, and then once the entity is "spawned" inside the engine, then the entity's 
own `spawn` method is called. 

You'll also notice that in the `spawn` method of `GameEntity`, if "initialized" is false for the entity, then the entity's `init` method is called and then
`initialized` is set to true. After this, the value for `initialized` will remain true until it is set to false by the programmer's own logic. 

Once this "spawn" process is complete, the entity is added to all systems that it qualifies for. In the `GameEngine` implementation, an entity is only added
to systems when the entity is first spawned. 

Each time the `GameEngine.update` method is called, entities that should be added are added to the engine along with any systems they qualify for, entities that should be 
removed are removed from the engine along with from all systems, and then finally each system is updated.

In `GameEntity`, calling the `kill` method sets the entity's `dead` field to true. When `dead` is true, then the entity is set to be removed from the engine and systems.

Let's see all of this in action. If you look in the "Megaman Maverick" project, you'll see that the game engine class is only updated when a "level screen is being shown.
We'll follow that design decision here by implementing a "game screen" class that is shown when the game is being played.

```kotlin

class MyGameScreen(private val gameEngine: GameEngine): ScreenAdapter() {

    override fun show() = resume()

    // for simplicity, we'll update the engine on every render
    override fun render(delta: Float) = gameEngine.update(delta)

    // for simplicity, on pause we'll turn all systems off
    override fun pause() = gameEngine.systems.forEach { it.on = false }

    // turn on all systems on resume
    override fun resume() = gameEngine.systems.forEach { it.on = true }

    override fun hide() = pause()

    // the reset method clears all entities out of the engine, clears all entities out of the systems, and then calls
    // the optional "reset" method for each system
    override fun dispose() = gameEngine.reset()
}

class MyGame: Game() {

    override fun create() {                  
        // create our entities and add them all to the engine in the "create" method 
        // in a real project, the logic for instantiating entities would be more complicated
        // since you likely would not want to spawn all of your entities at the beginning of the game
                   
        val entity1 = GameEntity()
        entity1.addComponent(FooComponent())
        entity1.addComponent(BarComponent())
                
        val entity2 = GameEntity()
        entity2.addComponent(FooComponent())
                      
        // initialize the game engine with the "foo sysytem" as its only system
        // in a real project, there would be many more systems to add here
        // spawn our entities using the game engine
                              
        val gameEngine = GameEngine(gdxArrayOf(FooSystem()))
        gameEngine.spawn(entity1, props("foo" to false, "bar" to true))
        gameEngine.spawn(entity2, props("foo" to true))       
        
        val gameScreen = MyGameScreen(gameEngine)       
        setScreen(gameScreen)
    }

    // render method in super class updates the current screen, which in this class is 'gameScreen'
}
```