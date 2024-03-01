package org.kkeunkkeun.pregen.common.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import java.awt.Color
import java.net.HttpURLConnection
import java.net.URL

class DiscordUtil(private val url: String) {
    private var content: String? = null
    private var username: String? = null
    private var avatarUrl: String? = null
    private var tts: Boolean = false
    private val embedObjects: MutableList<EmbedObject> = mutableListOf()

    fun addEmbed(embed: EmbedObject) {
        embedObjects.add(embed)
    }

    fun execute(objectMapper: ObjectMapper) {
        if (content == null && embedObjects.isEmpty()) {
            throw IllegalArgumentException("Set content or add at least one EmbedObject")
        }

        val webhookMessage = WebhookMessage(
            content = content,
            username = username,
            avatarUrl = avatarUrl,
            tts = tts,
            embeds = embedObjects.takeIf { it.isNotEmpty() }
        )

        val json = objectMapper.writeValueAsString(webhookMessage)

        with(URL(url).openConnection() as HttpURLConnection) {
            addRequestProperty("Content-Type", "application/json")
            addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_")
            doOutput = true
            requestMethod = "POST"

            outputStream.use { os ->
                os.write(json.toByteArray())
                os.flush()
            }

            inputStream.close()
            disconnect()
        }
    }

    data class WebhookMessage(
        val content: String?,
        val username: String?,
        val avatarUrl: String?,
        val tts: Boolean,
        val embeds: List<EmbedObject>?
    )

    data class EmbedObject(
        val title: String?,
        val description: String? = null,
        val url: String? = null,
        val color: Int?,
        val footer: Footer? = null,
        val thumbnail: Thumbnail? = null,
        val image: Image? = null,
        val author: Author? = null,
        val fields: List<Field>?
    ) {
        fun colorToInt(color: Color): Int {
            return color.red.shl(16) or color.green.shl(8) or color.blue
        }

        data class Footer(
            val text: String,
            val iconUrl: String?
        )

        data class Thumbnail(val url: String)

        data class Image(val url: String)

        data class Author(
            val name: String,
            val url: String?,
            val iconUrl: String?
        )

        data class Field(
            val name: String,
            val value: String,
            val inline: Boolean
        )
    }
}
