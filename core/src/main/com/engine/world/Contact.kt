package com.engine.world

data class Contact(val fixture1: Fixture, val fixture2: Fixture) {

  fun getFixturesInOrder(fixtureType1: String, fixtureType2: String) =
      if (fixture1.fixtureType == fixtureType1 && fixture2.fixtureType == fixtureType2) {
        Pair(fixture1, fixture2)
      } else if (fixture2.fixtureType == fixtureType1 && fixture1.fixtureType == fixtureType2) {
        Pair(fixture2, fixture1)
      } else null

  fun fixturesAreOfTypes(fixtureType1: String, fixtureType2: String) =
      (fixture1.fixtureType == fixtureType1 && fixture2.fixtureType == fixtureType2) ||
          (fixture2.fixtureType == fixtureType1 && fixture1.fixtureType == fixtureType2)

  override fun equals(other: Any?) =
      other is Contact &&
          ((fixture1 == other.fixture1 && fixture2 == other.fixture2) ||
              (fixture1 == other.fixture2 && fixture2 == other.fixture1))

  override fun hashCode() = 49 + 7 * fixture1.hashCode() + 7 * fixture2.hashCode()
}
