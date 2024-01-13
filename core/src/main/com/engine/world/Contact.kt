package com.engine.world

import com.badlogic.gdx.utils.ObjectSet

/**
 * A class that represents a contact between two [Fixture]s.
 *
 * @param fixture1 the first fixture
 * @param fixture2 the second fixture
 */
data class Contact(val fixture1: Fixture, val fixture2: Fixture) {

  /**
   * Returns if the fixtures have the given labels.
   *
   * @param fixtureLabel1 the label of the first fixture
   * @param fixtureLabel2 the label of the second fixture
   * @return if the fixtures are of the given labels
   */
  fun fixturesMatch(fixtureLabel1: Any, fixtureLabel2: Any) =
      (fixture1.fixtureLabel == fixtureLabel1 && fixture2.fixtureLabel == fixtureLabel2) ||
          (fixture2.fixtureLabel == fixtureLabel1 && fixture1.fixtureLabel == fixtureLabel2)

  /**
   * If [fixtureLabels1] contains the label of the first fixture and [fixtureLabels2] contains the
   * label of the second fixture, then true is returned. If [fixtureLabels2] contains the label of
   * the first fixture and [fixtureLabels1] contains the label of the second fixture, then true is
   * returned. Otherwise, false is returned.
   *
   * @param fixtureLabels1 the labels of the first fixture
   * @param fixtureLabels2 the labels of the second fixture
   * @return if the fixtures are of the given labels
   */
  fun fixtureSetsMatch(fixtureLabels1: ObjectSet<Any>, fixtureLabels2: ObjectSet<Any>) =
      (fixtureLabels1.contains(fixture1.fixtureLabel) &&
          fixtureLabels2.contains(fixture2.fixtureLabel)) ||
          (fixtureLabels1.contains(fixture2.fixtureLabel) &&
              fixtureLabels2.contains(fixture1.fixtureLabel))

  /**
   * Returns if at least one of the fixtures is of the given type.
   *
   * @param fixtureLabel the label of the fixture
   * @return if at least one of the fixtures is of the given type
   */
  fun oneFixtureMatches(fixtureLabel: Any) =
      fixture1.fixtureLabel == fixtureLabel || fixture2.fixtureLabel == fixtureLabel

  /**
   * If one of the fixtures has the [fixtureLabel], then the fixture with the [fixtureLabel] is
   * returned as the first element in the pair. If neither of the fixtures has the [fixtureLabel],
   * then null is returned.
   *
   * @param fixtureLabel the type of the fixture
   * @return the fixture with the [fixtureLabel] as the first element in the pair, or null if
   *   neither of the fixtures has the [fixtureLabel]
   */
  fun getFixturesIfOneMatches(fixtureLabel: Any) =
      if (fixture1.fixtureLabel == fixtureLabel) Pair(fixture1, fixture2)
      else if (fixture2.fixtureLabel == fixtureLabel) Pair(fixture2, fixture1) else null

  /**
   * Gets the fixtures in order of [fixtureLabel1] and [fixtureLabel2]. If the fixtures are not of
   * the given types, then null is returned.
   *
   * @param fixtureLabel1 the type of the first fixture
   * @param fixtureLabel2 the type of the second fixture
   * @return the fixtures in order of [fixtureLabel1] and [fixtureLabel2], or null if the fixtures
   */
  fun getFixturesInOrder(fixtureLabel1: Any, fixtureLabel2: Any) =
      if (fixture1.fixtureLabel == fixtureLabel1 && fixture2.fixtureLabel == fixtureLabel2)
          Pair(fixture1, fixture2)
      else if (fixture2.fixtureLabel == fixtureLabel1 && fixture1.fixtureLabel == fixtureLabel2)
          Pair(fixture2, fixture1)
      else null

  /**
   * If fixture1 has any label contained in [fixtureLabels1] and fixture2 has any contianed in
   * [fixtureLabels2], then the returned pair has fixture1 as the first element and fixture2 as the
   * second element. If fixture2 has any label contained in [fixtureLabels1] and fixture1 has any
   * contained in [fixtureLabels2], then the returned pair has fixture2 as the first element and
   * fixture1 as the second element. If neither of the fixtures have any of the labels, then null is
   * returned.
   *
   * @param fixtureLabels1 the labels of the first fixture
   * @param fixtureLabels2 the labels of the second fixture
   * @return the fixtures in order of [fixtureLabels1] and [fixtureLabels2], or null if the fixtures
   *   are not of the given types
   */
  fun getFixtureSetsInOrder(fixtureLabels1: ObjectSet<Any>, fixtureLabels2: ObjectSet<Any>) =
      if (fixtureLabels1.contains(fixture1.fixtureLabel) &&
          fixtureLabels2.contains(fixture2.fixtureLabel))
          Pair(fixture1, fixture2)
      else if (fixtureLabels1.contains(fixture2.fixtureLabel) &&
          fixtureLabels2.contains(fixture1.fixtureLabel))
          Pair(fixture2, fixture1)
      else null

  /**
   * Returns true if the given object is a [Contact] containing the same two fixtures. The order of
   * the fixtures does not matter.
   *
   * @param other the object to compare to
   * @return true if the given object is a [Contact] containing the same two fixtures
   */
  override fun equals(other: Any?) =
      other is Contact &&
          ((fixture1 == other.fixture1 && fixture2 == other.fixture2) ||
              (fixture1 == other.fixture2 && fixture2 == other.fixture1))

  override fun hashCode() = 49 + 7 * fixture1.hashCode() + 7 * fixture2.hashCode()

  override fun toString() = "Contact(fixture1=$fixture1, fixture2=$fixture2)"
}
