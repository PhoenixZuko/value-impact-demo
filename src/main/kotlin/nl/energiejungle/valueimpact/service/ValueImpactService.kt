package nl.energiejungle.valueimpact.service

import nl.energiejungle.valueimpact.model.*
import org.springframework.stereotype.Service

@Service
class ValueImpactService(
    private val bagClient: BagClient,
    private val energyLabelClient: EnergyLabelClient,
    private val wozClientMock: WozClientMock,
    private val calculator: ValueImpactCalculator
) {

    fun process(request: ValueImpactRequest): ValueImpactResponse {
        val bag = bagClient.lookupAddress(
            request.postcode,
            request.huisnummer ?: "",
            request.toevoeging ?: ""
        )

        val id = bag.nummeraanduidingId
            ?: throw IllegalArgumentException("Adresse niet gevonden")

        val woz = request.homeValue ?: wozClientMock.getWozValue(id)

        val label = request.energyLabel
            ?: energyLabelClient.fetchEnergyLabel(id).label
            ?: "Unknown"

        val range = calculator.calculateNewValueRange(woz, label)

        return ValueImpactResponse(
            nummeraanduidingId = id,
            wozValue = woz,
            energyLabel = label,
            estimatedNewValueRange = range
        )
    }
}
