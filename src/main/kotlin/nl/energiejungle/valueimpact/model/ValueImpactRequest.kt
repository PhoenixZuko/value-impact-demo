package nl.energiejungle.valueimpact.model

data class ValueImpactRequest(
    val postcode: String,
    val huisnummer: String,
    val toevoeging: String? = null,
    val homeValue: Double? = null,
    val energyLabel: String? = null
)
