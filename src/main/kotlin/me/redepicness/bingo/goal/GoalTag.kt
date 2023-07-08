package me.redepicness.bingo.goal

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.intellij.lang.annotations.Pattern
import java.util.*

class GoalTag private constructor(private val key: Key) : Key {
    init {
        tags[key] = this
    }

    @Pattern("[a-z0-9_\\-.]+")
    override fun namespace(): String = key.namespace()

    override fun value(): String = key.value()

    override fun asString(): String = key.asString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other !is GoalTag) false else key == other.key
    }

    override fun hashCode(): Int = key.hashCode()

    override fun toString() = asString()

    companion object {
        private val tags: MutableMap<Key, GoalTag> = HashMap()
        operator fun get(namespace: String, value: String): GoalTag {
            return Companion[NamespacedKey(namespace, value)]
        }

        operator fun get(key: Key): GoalTag {
            var tag = tags[key]
            if (tag == null) {
                tag = GoalTag(key)
            }
            return tag
        }

        val allTags: Collection<GoalTag> = tags.values
    }
}
