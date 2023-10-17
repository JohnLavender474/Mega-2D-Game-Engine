package com.engine.world

/** A class that represents a contact between two [Fixture]s. */
data class Contact(val fixture1: Fixture, val fixture2: Fixture) {

  /**
   * Gets the fixtures in order of [fixtureLabel1] and [fixtureLabel2]. If the fixtures are not of the
   * given types, then null is returned.
   *
   * @param fixtureLabel1 the type of the first fixture
   * @param fixtureLabel2 the type of the second fixture
   * @return the fixtures in order of [fixtureLabel1] and [fixtureLabel2], or null if the fixtures
   */
  fun getFixturesInOrder(fixtureLabel1: Any, fixtureLabel2: Any) =
      if (fixture1.fixtureLabel == fixtureLabel1 && fixture2.fixtureLabel == fixtureLabel2) {
        Pair(fixture1, fixture2)
      } else if (fixture2.fixtureLabel == fixtureLabel1 && fixture1.fixtureLabel == fixtureLabel2) {
        Pair(fixture2, fixture1)
      } else null

  /**
   * Returns if the fixtures are of the given types.
   *
   * @param fixtureLabel1 the type of the first fixture
   * @param fixtureLabel2 the type of the second fixture
   * @return if the fixtures are of the given types
   */
  fun fixturesAreOfTypes(fixtureLabel1: Any, fixtureLabel2: Any) =
      (fixture1.fixtureLabel == fixtureLabel1 && fixture2.fixtureLabel == fixtureLabel2) ||
          (fixture2.fixtureLabel == fixtureLabel1 && fixture1.fixtureLabel == fixtureLabel2)

  override fun equals(other: Any?) =
      other is Contact &&
          ((fixture1 == other.fixture1 && fixture2 == other.fixture2) ||
              (fixture1 == other.fixture2 && fixture2 == other.fixture1))

  override fun hashCode() = 49 + 7 * fixture1.hashCode() + 7 * fixture2.hashCode()
}
