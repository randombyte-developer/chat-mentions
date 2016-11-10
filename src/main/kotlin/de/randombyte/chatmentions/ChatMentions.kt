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
        val mentionRegex = (configManager.get().symbol + MINECRAFT_PLAYER_NAME_REGEX).toRegex()
        mentionRegex.findAll(event.rawMessage.toPlain())
                .filter { it.groupValues[1].isNotEmpty() }
                .forEach { mentionResult ->
                    val playerName = mentionResult.groupValues[1]
                    Sponge.getServer().getPlayer(playerName).ifPresent { player ->
                        // TODO
                    }
                }
    }
}