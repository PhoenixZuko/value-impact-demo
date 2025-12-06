# Value Impact Demo

This is a Kotlin + Spring Boot demo backend that estimates the potential increase in house value
after installing a heat pump. The system retrieves (or mocks) official housing data and calculates
an estimated new value range based on the current energy label.

---

## 🧱 Project Architecture

src/
└── main/
├── kotlin/
│    └── nl.energiejungle.valueimpact/
│           ├── controller/          # REST endpoints
│           ├── service/             # business logic + external clients
│           ├── model/               # request/response DTOs
│           └── config/              # external API configuration
└── resources/                        # Spring configuration

### Layer responsibilities
- Controller: exposes `/api/value-impact`
- Service: orchestrates the full workflow
- Clients: BAG (real API), WOZ (mock), EnergyLabel (mock fallback)
- Calculator: computes value increase range
- Models: request/response structures

---

## 🚀 Requirements & Environment

This demo was generated using Spring Initializr:

- Build tool: **Gradle (Kotlin DSL)**
- Language: **Kotlin**
- Spring Boot: **3.5.8**
- Java: **17**
- Packaging: JAR
- Config: YAML

### Dependencies
- Spring Web
- Spring Boot DevTools
- Spring Configuration Processor

### Runtime Requirements
- Java 17+
- Internet access (for BAG API)
- No database required
- No external API keys required
- Gradle wrapper included (no installation needed)

---

## 🛠️ Installation & Running

### 1. Clone the project
git clone <repo-url>
cd value-impact-demo

### 2. Run the application
Linux/macOS:
./gradlew bootRun

Windows:
gradlew.bat bootRun

On startup you will see:
"Tomcat started on port 9090"

API available at:
http://localhost:9090

---

## 📡 Main Endpoint

POST http://localhost:9090/api/value-impact

### Example request
{
"postcode": "3011RS",
"huisnummer": "9",
"toevoeging": "C",
"homeValue": null,
"energyLabel": null
}

### Example response
{
"nummeraanduidingId": "0599200000331645",
"wozValue": 441000.0,
"energyLabel": "B",
"estimatedNewValueRange": {
"min": 463050.0,
"max": 472311.0
}
}

---

## 🌍 External Integrations

### BAG API (real)
The demo uses the real public BAG endpoint to resolve the officiële nummeraanduidingId.

### WOZ API (mock)
The official WOZwaardeloket API requires session-handshake and restricted access.  
For this demo, WOZ values are returned by a stable mock (441000.0).

### EnergyLabel API (mock fallback)
Overheid.io requires an authenticated API key.  
If no key is configured, the demo automatically falls back to a predictable mock label.

All mock implementations contain a technical note and can be replaced with the real API clients
with minimal changes.

---

## 🧮 Value Calculation Logic

The ValueImpactCalculator applies a multiplier based on the energy label:

- A+, A++, A+++, A++++ → higher multipliers
- A, B, C → medium multipliers
- D–G → lower multipliers
- Unknown → minimal impact

Output is returned as:
{
"min": value * multiplier,
"max": value * multiplier * 1.02
}

---

## 🧪 How to Verify the Demo

1. Start the application
2. Send the POST request shown above
3. Confirm response includes:
    - nummeraanduidingId (from BAG real API)
    - mock WOZ value
    - mock or provided energyLabel
    - computed new value range

No database or external credentials needed.

---

## 📎 Notes for Technical Reviewers

- BAG lookup is fully operational using the real PDOK service.
- WOZ and EnergyLabel are mocked due to restricted API access.
- Architecture is modular and production-ready.
- Replacing mocks with real API clients requires minimal changes.
- All logic is executed in-memory; no persistence layer included.

---

## ✔ Summary

This demo delivers the complete workflow: address resolution, housing value retrieval (mocked where required),
energy label interpretation, and new value estimation.
It is lightweight, easy to run, and prepared for real API integration when access becomes available.


## Testing the API

You can test the demo by sending a POST request to:

```
POST http://localhost:9090/api/value-impact
```

### Example request
```json
{
  "postcode": "3011RS",
  "huisnummer": "9",
  "toevoeging": "C",
  "homeValue": null,
  "energyLabel": null
}
```

### Example response
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

This confirms the full workflow is functioning end-to-end using real BAG lookup and mock fallbacks for WOZ and EnergyLabel.
