package de.randombyte.chatmentions

import com.google.inject.Inject
import de.randombyte.kosp.config.ConfigManager
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers

@Plugin(id = ChatMentions.ID, name = ChatMentions.NAME, version = ChatMentions.VERSION, authors = arrayOf(ChatMentions.AUTHOR))
class ChatMentions @Inject constructor(
        val logger: Logger,
        @DefaultConfig(sharedRoot = true) configLoader: ConfigurationLoader<CommentedConfigurationNode>) {

    companion object {
        const val NAME = "ChatMentions"
        const val ID = "chatmentions"
        const val VERSION = "v0.1"
        const val AUTHOR = "RandomByte"

        const val MINECRAFT_PLAYER_NAME_REGEX = "[a-zA-Z0-9_]{1,16}"
    }

    val configManager = ConfigManager(configLoader, Config::class.java)

    @Listener
    fun onInit(event: GameInitializationEvent) {
        logger.info("Loaded $NAME: $VERSION")
    }

    @Listener
    fun onChat(event: MessageChannelEvent.Chat) {
        event.formatter.setBody(correctPlayerNames(event.rawMessage))
    }

    /**
     * Makes sure the playernames are capitalized correctly, e.g. "kitcrAfty" -> "KitCrafty".
     * Side-effect: Removes any styles of the text(like color).
     */
    fun correctPlayerNames(text: Text): Text {
        val symbol = configManager.get().symbol
        val words = TextSerializers.FORMATTING_CODE.serialize(text).split(" ")
        val newWords = words.map { word ->
            if (word.startsWith(symbol)) {
                val playerName = word.split(symbol)[1]
                if (playerName.isNotEmpty()) {
                    val optPlayer = Sponge.getServer().getPlayer(playerName)
                    if (optPlayer.isPresent) {
                        return@map symbol + optPlayer.get().name
                    }
                }
            }

            return@map word
        }.joinToString(separator = " ")

        return Text.of(newWords)
    }
}