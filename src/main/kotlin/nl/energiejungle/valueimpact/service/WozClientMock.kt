package nl.energiejungle.valueimpact.service

import org.springframework.stereotype.Service

@Service
class WozClientMock {

    // TECHNICAL NOTE:
    // This mock simulates the WOZ value lookup. In the production version,
    // this will be replaced with an integration with the official
    // WOZwaardeloket API (session handshake + nummeraanduiding lookup).
    // For the demo, we return a fixed value to keep the flow stable.

    fun getWozValue(nummeraanduidingId: String): Double {
        return 441000.0 // mock WOZ value for demonstration
    }
}
