package nl.energiejungle.valueimpact.model

data class ValueImpactResponse(
    val nummeraanduidingId: String,
    val wozValue: Double,
    val energyLabel: String,
    val estimatedNewValueRange: Pair<Double, Double>
)

