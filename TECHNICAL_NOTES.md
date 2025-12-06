This demo implements the complete backend workflow for estimating the value increase of a house
after installing a heat pump. BAG address lookup is real, while WOZ and EnergieLabel integrations
use stable mock fallbacks due to external API access limitations.
The architecture is modular and ready for production integration with minimal changes.

```text
              +-----------------------------+
              |     ValueImpactController   |
              |     POST /value-impact      |
              +---------------+-------------+
                              |
                              v
                 +---------------------------+
                 |     ValueImpactService    |
                 |  core business workflow   |
                 +---+------------+----------+
                     |            |
     +---------------+            +------------------+
     |                                                |
     v                                                v
+------------+                               +----------------+
| BagClient  |  --> BAG API (real)          | WozClientMock  |
| lookupAddress()                            | getWozValue()  |
| returns nummeraanduidingId                 | returns 441k   |
+------------+                               +----------------+
                     |
                     v
            +----------------------+
            | EnergyLabelClient   |
            | fetchEnergyLabel()  |
            | MOCK fallback       |
            +----------------------+
                     |
                     v
        +-------------------------------+
        |   ValueImpactCalculator       |
        |  computes new value range     |
        +-------------------------------+
                     |
                     v
          +-------------------------------+
          |  ValueImpactResponse (JSON)   |
          +-------------------------------+
```

**Project requirements:**  
Retrieve official house data from three government sources (BAG, WOZ, EnergyLabel),
store relevant results, and calculate the estimated value increase after installing a heat pump.

**Demo implementation:**  
BAG address lookup is implemented using the real API. WOZ and EnergyLabel use mock clients because
the official services require restricted access and production API keys.  
Database storage is not included in the demo; values are processed in-memory, but the architecture
is prepared for persistence when required.

**Result:**  
The full workflow is functional end-to-end. Mock clients can be replaced with real API integrations
and database storage with minimal structural changes.

**Additional notes:**  
The demo focuses on clean architecture, clear separation of responsibilities, and a predictable data flow.  
Error handling, fallback logic, and request validation are included to ensure stable behaviour.  
The system is lightweight, easy to run locally, and prepared for future expansion once access to real APIs
and database integration becomes available.

## How the demo was tested

The system was tested locally using PowerShell (curl), Postman, and IntelliJ HTTP client.
The goal was to validate the full workflow: BAG lookup → WOZ mock → EnergyLabel mock → value calculation.

### PowerShell test command
```powershell
curl -Method POST "http://localhost:9090/api/value-impact" `
  -Headers @{ "Content-Type" = "application/json" } `
  -Body '{"postcode":"3011RS","huisnummer":"9","toevoeging":"C","energyLabel":null,"homeValue":null}'
```

### Actual result obtained during testing
```json
{
  "nummeraanduidingId": "0599200000331645",
  "wozValue": 441000.0,
  "energyLabel": "B",
  "estimatedNewValueRange": {
    "first": 463050.0,
    "second": 472311.0
  }
}
```

### What this validates
- BAG lookup using the real PDOK API is working correctly.
- WOZ value is returned via a stable mock due to restricted API access.
- EnergyLabel returns a fallback mock label when no API key is configured.
- The ValueImpactCalculator produces a predictable and deterministic range.
- The system responds quickly (< 50ms locally) and behaves consistently.

This confirms the demo is fully functional end-to-end and ready for production API integration with minimal modifications.
