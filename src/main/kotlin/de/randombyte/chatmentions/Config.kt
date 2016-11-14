package de.randombyte.chatmentions

import com.google.common.reflect.TypeToken
import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer
import org.spongepowered.api.CatalogType
import org.spongepowered.api.effect.sound.SoundType
import java.util.*

@ConfigSerializable
data class Config(
    @Setting val symbol: String = "@",
    @Setting val sounds: List<String> = listOf("minecraft:entity.arrow.hit_player",
            "minecraft:entity.player.levelup player"),
    @Setting val defaultSound: String = "minecraft:entity.arrow.hit_player",
    @Setting val userPreferences: Map<UUID, UserPreference> = emptyMap()
)

@ConfigSerializable
data class UserPreference(
    @Setting val sound: String
) {
    fun isSoundEnabled() = sound.isNotEmpty()
}

object OptionalSoundTypeTypeSerializer : TypeSerializer<SoundType> {
    override fun deserialize(type: TypeToken<*>, value: ConfigurationNode): SoundType? =
            if (value.string.isEmpty()) null else value.getValue(TypeToken.of(CatalogType::class.java)) as SoundType

    override fun serialize(type: TypeToken<*>, obj: SoundType, value: ConfigurationNode) {
        value.setValue(TypeToken.of(CatalogType::class.java), obj)
    }
}