package de.randombyte.chatmentions

import de.randombyte.kosp.config.ConfigManager
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.ChatTypeMessageReceiver
import org.spongepowered.api.text.channel.MessageChannel
import org.spongepowered.api.text.channel.MessageReceiver
import org.spongepowered.api.text.chat.ChatType

class MentionsChannel(
        val receivers: MutableCollection<MessageReceiver>,
        val configManager: ConfigManager<Config>) : MessageChannel {

    override fun getMembers() = receivers

    override fun send(sender: Any?, original: Text, type: ChatType) {
        val config = configManager.get()
        members.forEach { member ->
            if (member is ChatTypeMessageReceiver) {
                transformMessage(sender, member, original, type).ifPresent { member.sendMessage(type, it) }
            } else {
                transformMessage(sender, member, original, type).ifPresent { member.sendMessage(it) }
            }
            if (member is Player && config.userPreferences[member.uniqueId]!!.isSoundEnabled() &&
                original.contains(configManager.get().symbol + member.name)) {
                //member.playSound()
            }
        }
    }

    private fun Text.contains(other: String) = toPlain().contains(other)
}