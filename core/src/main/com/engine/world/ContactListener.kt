package com.engine.world

interface ContactListener {
  fun beginContact(f1: Fixture, f2: Fixture, delta: Float)

  fun continueContact(f1: Fixture, f2: Fixture, delta: Float)

  fun endContact(f1: Fixture, f2: Fixture, delta: Float)
}
