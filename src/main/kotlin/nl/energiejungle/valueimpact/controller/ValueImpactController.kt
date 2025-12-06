package nl.energiejungle.valueimpact.controller

import nl.energiejungle.valueimpact.model.ValueImpactRequest
import nl.energiejungle.valueimpact.model.ValueImpactResponse
import nl.energiejungle.valueimpact.service.ValueImpactService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/value-impact")
class ValueImpactController(
    private val service: ValueImpactService
) {

    @PostMapping
    fun calculate(@RequestBody request: ValueImpactRequest): ResponseEntity<ValueImpactResponse> {
        val result = service.process(request)
        return ResponseEntity.ok(result)
    }
}
