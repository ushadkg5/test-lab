# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Stack

- Java 17 (`maven.compiler.release=17`)
- Maven multi-module build; all dependency and plugin versions are managed in the root `pom.xml`
- JUnit 5 (Jupiter), AssertJ, REST Assured, Mockito
- Surefire 3.x (pinned in the parent)

## Modules

- `sample-app` — the application under test (books API)
- `api-tests` — REST Assured tests against the books API
- `flaky-demo` — examples of flaky tests and their fixes

## Commands

- `mvn -B verify` — build and run everything
- `mvn -pl <module> test` — run one module's tests, e.g. `mvn -pl sample-app test`
- `api-tests` depends on `sample-app`, so add `-am`: `mvn -pl api-tests -am test`
- `mvn -pl sample-app spring-boot:run` — start the books API on port 8080
- `mvn -pl flaky-demo test -Pflaky` — run only the intentionally flaky tests (expected to fail)

## flaky-demo

Each pattern has a `*FlakyTest` (tagged `flaky`, broken on purpose) and a `*Test` with the fix.
`flaky`-tagged tests are excluded from the normal build. Don't "fix" them, and don't copy their patterns.

| Pattern | Flaky because | Fix |
|---|---|---|
| `OrderProcessor` | asserts before async work finishes | Awaitility `await().atMost(...)` |
| `Greeter` | uses the real system clock | inject a fixed `Clock` |
| `VisitCounter` | static state shared between tests | fresh instance in `@BeforeEach` |
| `TagService` | assumes `Set` iteration order | `containsExactlyInAnyOrder` |

## Books API implementation

Spring Boot 3 app in `sample-app`, in-memory storage. `api-tests` starts it on a random port with
`@SpringBootTest` and calls it with REST Assured. Each test creates its own books with unique ISBNs.

## Testing rules

- **No `Thread.sleep`.** Wait for a condition explicitly (poll with a timeout) instead of sleeping for a fixed time.
- **One behavior per test.** A test checks a single behavior; split tests that verify unrelated things.
- **Tests are independent.** Each test sets up its own data and must pass when run alone or in any order. No shared mutable state between tests, no reliance on execution order.
- **Names describe behavior.** E.g. `returns404WhenBookIdIsUnknown`, not `testGetBook2`.
- Use AssertJ (`assertThat`) for assertions, not JUnit's `assertEquals`.
- Do not add dependency versions in module poms; add them to `dependencyManagement` in the parent.

## Books API rules

Validation:
- `title` is required, max 255 characters.
- `author` is required.
- `isbn` is required, must be exactly 10 or 13 digits and unique; a duplicate ISBN returns **409 Conflict**.
- `price` is required and must be 0 or more.
- Any invalid input returns **400 Bad Request**.

Lookup:
- An unknown book ID returns **404 Not Found**.

Auth:
- `POST`, `PUT` and `DELETE` require `Authorization: Bearer test-token`.
- A missing or wrong token returns **401 Unauthorized**.
- `GET` is public (no token needed).
