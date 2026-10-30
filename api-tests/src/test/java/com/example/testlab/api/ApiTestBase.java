package com.example.testlab.api;

import static io.restassured.RestAssured.given;

import com.example.testlab.SampleApplication;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Starts the books API on a random port. Each test builds its own request spec
 * (no global RestAssured state) and creates its own books with unique ISBNs, so
 * tests can run in any order.
 */
@SpringBootTest(classes = SampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ApiTestBase {

    static final String VALID_TOKEN = "test-token";
    static final long UNKNOWN_ID = Long.MAX_VALUE;

    private static final AtomicLong NEXT_ISBN_13 = new AtomicLong(9_780_000_000_000L);
    private static final AtomicLong NEXT_ISBN_10 = new AtomicLong(1_000_000_000L);

    @LocalServerPort
    private int port;

    RequestSpecification anonymous() {
        return given().port(port).basePath("/books").contentType(ContentType.JSON);
    }

    RequestSpecification authorized() {
        return anonymous().header("Authorization", "Bearer " + VALID_TOKEN);
    }

    static String uniqueIsbn13() {
        return String.valueOf(NEXT_ISBN_13.getAndIncrement());
    }

    static String uniqueIsbn10() {
        return String.valueOf(NEXT_ISBN_10.getAndIncrement());
    }

    /** A valid request body; mutable so tests can change or remove a single field. */
    static Map<String, Object> validBook() {
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Dune");
        body.put("author", "Frank Herbert");
        body.put("isbn", uniqueIsbn13());
        body.put("price", new BigDecimal("9.99"));
        return body;
    }

    long createBook(Map<String, Object> body) {
        return authorized().body(body)
                .when().post()
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    long createBook() {
        return createBook(validBook());
    }
}
