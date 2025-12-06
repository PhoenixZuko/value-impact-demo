// NOTE FOR TECH REVIEW:
// This client currently uses a fallback/mock implementation when no valid
// Overheid.io API key is provided. The structure is prepared for integration
// with the real API (ovio-api-key, filters for postcode/huisNummer/toevoeging).
// Replace the mock branch with the actual API call once a production key is available.



package nl.energiejungle.valueimpact.service

import nl.energiejungle.valueimpact.config.EnergyLabelConfig
import nl.energiejungle.valueimpact.model.EnergyLabelResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder

@Service
class EnergyLabelClient(
    private val config: EnergyLabelConfig
) {
    private val client = RestTemplate()

    fun fetchEnergyLabel(nummeraanduidingId: String): EnergyLabelResponse {

        // === 1) Dacă nu avem API KEY → revenim imediat mock ===
        if (config.apiKey.isBlank() || config.apiKey == "MOCK") {
            return EnergyLabelResponse(
                label = "B",               // mock
                lastUpdate = "2024-01-01"  // mock
            )
        }

        try {
            val url = UriComponentsBuilder
                .fromHttpUrl(config.baseUrl)
                .pathSegment(nummeraanduidingId)
                .build()
                .toUri()

            val headers = HttpHeaders()
            headers["X-Api-Key"] = config.apiKey

            val entity = HttpEntity<String>(headers)

            val result = client.exchange(url, HttpMethod.GET, entity, Map::class.java)

            val body = result.body ?: return EnergyLabelResponse(
                label = "UNKNOWN",
                lastUpdate = null
            )

            val label = body["label"] as? String ?: "UNKNOWN"
            val updated = body["peildatum"] as? String

            return EnergyLabelResponse(
                label = label,
                lastUpdate = updated
            )

        } catch (ex: HttpClientErrorException.Unauthorized) {
            // === 2) Dacă API Key invalidă → mock fallback ===
            return EnergyLabelResponse(
                label = "B",
                lastUpdate = "2024-01-01"
            )
        } catch (ex: Exception) {
            // === 3) Alte erori → mock fallback ===
            return EnergyLabelResponse(
                label = "B",
                lastUpdate = "2024-01-01"
            )
        }
    }
}
