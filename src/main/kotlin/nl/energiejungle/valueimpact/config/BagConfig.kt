package nl.energiejungle.valueimpact.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bag")
data class BagConfig(
    val baseUrl: String
)
