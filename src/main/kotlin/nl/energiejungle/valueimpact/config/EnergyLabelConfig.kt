package nl.energiejungle.valueimpact.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "energielabel")
data class EnergyLabelConfig(
    val baseUrl: String,
    val apiKey: String
)
