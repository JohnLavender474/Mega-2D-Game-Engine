package com.engine.world

/** A class that represents a contact between two [Fixture]s. */
data class Contact(val fixture1: Fixture, val fixture2: Fixture) {

  /**
   * Gets the fixtures in order of [fixtureType1] and [fixtureType2]. If the fixtures are not of the
   * given types, then null is returned.
   *
   * @param fixtureType1 the type of the first fixture
   * @param fixtureType2 the type of the second fixture
   * @return the fixtures in order of [fixtureType1] and [fixtureType2], or null if the fixtures
   */
  fun getFixturesInOrder(fixtureType1: String, fixtureType2: String) =
      if (fixture1.fixtureType == fixtureType1 && fixture2.fixtureType == fixtureType2) {
        Pair(fixture1, fixture2)
      } else if (fixture2.fixtureType == fixtureType1 && fixture1.fixtureType == fixtureType2) {
        Pair(fixture2, fixture1)
      } else null

  /**
   * Returns if the fixtures are of the given types.
   *
   * @param fixtureType1 the type of the first fixture
   * @param fixtureType2 the type of the second fixture
   * @return if the fixtures are of the given types
   */
  fun fixturesAreOfTypes(fixtureType1: String, fixtureType2: String) =
      (fixture1.fixtureType == fixtureType1 && fixture2.fixtureType == fixtureType2) ||
          (fixture2.fixtureType == fixtureType1 && fixture1.fixtureType == fixtureType2)

  override fun equals(other: Any?) =
      other is Contact &&
          ((fixture1 == other.fixture1 && fixture2 == other.fixture2) ||
              (fixture1 == other.fixture2 && fixture2 == other.fixture1))

  override fun hashCode() = 49 + 7 * fixture1.hashCode() + 7 * fixture2.hashCode()
}
