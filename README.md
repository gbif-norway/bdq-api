# BDQ API

REST API wrapper for [FilteredPush](https://github.com/FilteredPush) biodiversity data quality validation libraries. FilteredPush provides implementations of BDQ (Biodiversity Data Quality) Tests via the FFDQ API. This API uses those libraries directly (no custom reimplementation), mapping their responses into a simple JSON shape for convenience. The BDQ standard defines a library of Tests documented in the TDWG BDQ repository, extracted here is`TG2_tests.csv`.

## Overview

This API provides REST endpoints backed by the FilteredPush BDQ libraries:

- `geo_ref_qc` — Geographic coordinate and location validation
- `sci_name_qc` — Scientific name and taxonomy validation
- `event_date_qc` — Date and time validation/standardization
- `rec_occur_qc` — Record-level occurrence metadata validation

## Quick Start

### Using Docker Compose (Recommended)

```bash
git clone <your-repo>
cd bdq-api
git submodule update --init --recursive
docker-compose up --build
```

The API will be available at `http://localhost:8080`

### Manual Setup

1. **Clone with submodules:**
   ```bash
   git clone --recursive <your-repo>
   cd bdq-api
   ```

2. **Build FilteredPush libraries:** (optional; Docker build also does this)
   ```bash
   cd lib/geo_ref_qc && mvn clean package -DskipTests && cd ../..
   cd lib/sci_name_qc && mvn clean package -DskipTests && cd ../..
   cd lib/event_date_qc && mvn clean package -DskipTests && cd ../..
   cd lib/rec_occur_qc && mvn clean package -DskipTests && cd ../..
   ```

3. **Run the API:**
   ```bash
   mvn spring-boot:run
   ```

## API Documentation

Interactive API documentation is available at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Endpoints

- `GET /api/v1/tests` — List discovered BDQ tests from FilteredPush libraries.
- `POST /api/v1/tests/run` — Run a test by BDQ label or GUID, providing parameters as a map.

## Response Format

All endpoints return a consistent response format:

```json
{
  "status": "RUN_HAS_RESULT",
  "result": "COMPLIANT",
  "comment": "dwc:countryCode is a valid ISO (ISO 3166-1-alpha-2 country codes) value"
}
```

**Status values:**
- `RUN_HAS_RESULT` - Completed run with a result
- `AMENDED` - Proposed standardized/corrected value
- `NOT_AMENDED` - No unambiguous amendment proposed
- `FILLED_IN` - Populated a missing value
- `EXTERNAL_PREREQUISITES_NOT_MET` - External service unavailable
- `INTERNAL_PREREQUISITES_NOT_MET` - Input missing/invalid for the test
- `AMBIGUOUS` - Inputs produce ambiguous outcome (no amendment)

**Result values:**
- `COMPLIANT` - Passes validation
- `NOT_COMPLIANT` - Fails validation
- `POTENTIAL_ISSUE` / `NOT_ISSUE` - Issue signal (for Issue-type tests)

## Health Check

- `GET /actuator/health` - Application health status

## Contributing

The FilteredPush libraries are included as Git submodules and built locally by Docker. To update them:

```bash
git submodule update --remote
```

## License

This API wrapper is provided under the same license terms as the underlying FilteredPush libraries.
