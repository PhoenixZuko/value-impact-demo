package nl.energiejungle.valueimpact

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import nl.energiejungle.valueimpact.config.BagConfig
import nl.energiejungle.valueimpact.config.EnergyLabelConfig

@SpringBootApplication
@EnableConfigurationProperties(
	BagConfig::class,
	EnergyLabelConfig::class
)
class ValueImpactDemoApplication

fun main(args: Array<String>) {
	runApplication<ValueImpactDemoApplication>(*args)
}
