package com.mega.game.engine.world.contacts

import com.badlogic.gdx.utils.ObjectSet
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.world.body.IFixture

/**
 * A class that represents a contact between two [IFixture]s. This class is intended to be used with
 * [IFixture]s.
 *
 * @param fixture1 the first fixture
 * @param fixture2 the second fixture
 */
data class Contact(var fixture1: IFixture, var fixture2: IFixture) {

    /**
     * Sets the fixtures contained in this [Contact].
     *
     * @param fixture1 the first fixture
     * @param fixture2 the second fixture
     */
    fun set(fixture1: IFixture, fixture2: IFixture) {
        this.fixture1 = fixture1
        this.fixture2 = fixture2
    }

    /**
     * Returns if the fixtures have the given labels.
     *
     * @param fixtureType1 the label of the first fixture
     * @param fixtureType2 the label of the second fixture
     * @return if the fixtures are of the given labels
     */
    fun fixturesMatch(fixtureType1: Any, fixtureType2: Any) =
        (fixture1.getType() == fixtureType1 && fixture2.getType() == fixtureType2) ||
                (fixture2.getType() == fixtureType1 && fixture1.getType() == fixtureType2)

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
        (fixtureLabels1.contains(fixture1.getType()) && fixtureLabels2.contains(fixture2.getType())) ||
                (fixtureLabels1.contains(fixture2.getType()) && fixtureLabels2.contains(fixture1.getType()))

    /**
     * Returns if at least one of the fixtures is of the given type.
     *
     * @param fixtureType the label of the fixture
     * @return if at least one of the fixtures is of the given type
     */
    fun oneFixtureMatches(fixtureType: Any) =
        fixture1.getType() == fixtureType || fixture2.getType() == fixtureType

    /**
     * If one of the fixtures has the [fixtureType], then the fixture with the [fixtureType] is
     * returned as the first element in the pair. If neither of the fixtures has the [fixtureType],
     * then null is returned.
     *
     * @param fixtureType the type of the fixture
     * @return the fixture with the [fixtureType] as the first element in the pair, or null if
     *   neither of the fixtures has the [fixtureType]
     */
    fun getFixturesIfOneMatches(fixtureType: Any) =
        when (fixtureType) {
            fixture1.getType() -> GamePair(fixture1, fixture2)
            fixture2.getType() -> GamePair(fixture2, fixture1)
            else -> null
        }

    /**
     * Gets the fixtures in order of [fixtureType1] and [fixtureType2]. If the fixtures are not of
     * the given types, then null is returned.
     *
     * @param fixtureType1 the type of the first fixture
     * @param fixtureType2 the type of the second fixture
     * @return the fixtures in order of [fixtureType1] and [fixtureType2], or null if the fixtures
     */
    fun getFixturesInOrder(fixtureType1: Any, fixtureType2: Any) =
        if (fixture1.getType() == fixtureType1 && fixture2.getType() == fixtureType2) GamePair(
            fixture1,
            fixture2
        )
        else if (fixture2.getType() == fixtureType1 && fixture1.getType() == fixtureType2) GamePair(
            fixture2,
            fixture1
        )
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
        if (fixtureLabels1.contains(fixture1.getType()) &&
            fixtureLabels2.contains(fixture2.getType())
        )
            GamePair(
                fixture1,
                fixture2
            )
        else if (fixtureLabels1.contains(fixture2.getType()) &&
            fixtureLabels2.contains(fixture1.getType())
        )
            GamePair(
                fixture2,
                fixture1
            )
        else null

    /**
     * Returns true if the given object is a [Contact] containing the same two fixtures. The order of
     * the fixtures does not matter.
     *
     * @param other the object to compare to
     * @return true if the given object is a [Contact] containing the same two fixtures
     */
    override fun equals(other: Any?) =
        other is Contact && ((fixture1 == other.fixture1 && fixture2 == other.fixture2) ||
                (fixture1 == other.fixture2 && fixture2 == other.fixture1))

    override fun hashCode() = 49 + 7 * fixture1.hashCode() + 7 * fixture2.hashCode()

    override fun toString() = "Contact(fixture1=$fixture1, fixture2=$fixture2)"
}
