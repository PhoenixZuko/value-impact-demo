package nl.energiejungle.valueimpact.service

import org.springframework.stereotype.Service

@Service
class ValueImpactCalculator {

    fun calculateNewValueRange(currentValue: Double, energyLabel: String): Pair<Double, Double> {

        val label = energyLabel.uppercase().trim()

        val multiplier = when {
            label.startsWith("A+++") -> 1.12
            label.startsWith("A++")  -> 1.11
            label.startsWith("A+")   -> 1.10
            label == "A"             -> 1.08
            label == "B"             -> 1.05
            label == "C"             -> 1.03
            label in listOf("D","E","F","G") -> 1.02
            else -> 1.01   // unknown / undefined
        }

        val min = currentValue * multiplier
        val max = min * 1.02

        return Pair(min, max)
    }
}
