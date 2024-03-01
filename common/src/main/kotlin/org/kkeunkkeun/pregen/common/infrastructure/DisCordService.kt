package org.kkeunkkeun.pregen.common.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.kkeunkkeun.pregen.common.infrastructure.config.CommonProperties
import org.kkeunkkeun.pregen.common.presentation.PregenException
import org.springframework.stereotype.Component
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class DisCordService(
    private val objectMapper: ObjectMapper,
    private val commonProperties: CommonProperties,
) {

    fun sendDiscordAlertLog(e: PregenException, request: HttpServletRequest) {
        val discordBot = DiscordUtil(commonProperties.url)
        val timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())

        val embed = DiscordUtil.EmbedObject(
            title = "\uD83D\uDEA8",
            color = Color.RED.red,
            fields = listOf(
                addField("Request IP", request.remoteAddr, true),
                addField("Error Code", e.errorStatus.status.value().toString(), true),
                addField("Error Message", e.errorStatus.message!!, true),
                addField("timestamp", timestamp, true),
                addField("Path", "${request.requestURL} ${request.method}", true),
            )
        )
        discordBot.addEmbed(embed)
        discordBot.execute(objectMapper)
    }

    private fun addField(name: String, value: String, inline: Boolean) = DiscordUtil.EmbedObject.Field(
        name = name,
        value = value,
        inline = inline
    )
}
