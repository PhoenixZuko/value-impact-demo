package nl.energiejungle.valueimpact.service

import nl.energiejungle.valueimpact.config.BagConfig
import nl.energiejungle.valueimpact.model.BagResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class BagClient(
    private val config: BagConfig
) {
    private val client = RestTemplate()

    fun lookupAddress(postcode: String, huisnummer: String, toevoeging: String?): BagResponse {

        if (huisnummer.isBlank()) {
            return BagResponse(null)
        }

        val query = buildString {
            append("postcode:$postcode AND huisnummer:$huisnummer")
            if (!toevoeging.isNullOrBlank()) {
                append(" AND huisletter:$toevoeging")
            }
        }

        val uri = UriComponentsBuilder
            .fromHttpUrl(config.baseUrl)
            .queryParam("q", query)
            .queryParam("fl", "nummeraanduiding_id")
            .build()
            .toUri()

        val json = runCatching {
            client.getForObject(uri, Map::class.java)
        }.getOrNull() ?: return BagResponse(null)

        val docs = ((json["response"] as? Map<*, *>)?.get("docs") as? List<Map<String, Any?>>)
        val id = docs?.firstOrNull()?.get("nummeraanduiding_id") as? String

        return BagResponse(id)
    }
}
