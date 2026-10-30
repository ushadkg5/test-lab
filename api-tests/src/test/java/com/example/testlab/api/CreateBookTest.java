package com.example.testlab.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.path.json.JsonPath;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CreateBookTest extends ApiTestBase {

    @Test
    void returns201WithCreatedBook() {
        Map<String, Object> body = validBook();

        JsonPath json = authorized().body(body).when().post().then().statusCode(201).extract().jsonPath();

        assertThat(json.getLong("id")).isPositive();
        assertThat(json.getString("title")).isEqualTo("Dune");
        assertThat(json.getString("author")).isEqualTo("Frank Herbert");
        assertThat(json.getString("isbn")).isEqualTo(body.get("isbn"));
        assertThat(json.getDouble("price")).isEqualTo(9.99);
    }

    @Test
    void returns400WhenTitleIsMissing() {
        Map<String, Object> body = validBook();
        body.remove("title");

        authorized().body(body).when().post().then().statusCode(400);
    }

    @Test
    void returns400WhenTitleIsLongerThan255Characters() {
        Map<String, Object> body = validBook();
        body.put("title", "a".repeat(256));

        authorized().body(body).when().post().then().statusCode(400);
    }

    @Test
    void acceptsTitleOf255Characters() {
        Map<String, Object> body = validBook();
        body.put("title", "a".repeat(255));

        authorized().body(body).when().post().then().statusCode(201);
    }

    @Test
    void returns400WhenAuthorIsMissing() {
        Map<String, Object> body = validBook();
        body.remove("author");

        authorized().body(body).when().post().then().statusCode(400);
    }

    @Test
    void returns400WhenIsbnIsNotTenOrThirteenDigits() {
        Map<String, Object> body = validBook();
        body.put("isbn", "123456789012");

        authorized().body(body).when().post().then().statusCode(400);
    }

    @Test
    void returns400WhenIsbnIsMissing() {
        Map<String, Object> body = validBook();
        body.remove("isbn");

        authorized().body(body).when().post().then().statusCode(400);
    }

    @Test
    void acceptsTenDigitIsbn() {
        Map<String, Object> body = validBook();
        body.put("isbn", uniqueIsbn10());

        authorized().body(body).when().post().then().statusCode(201);
    }

    @Test
    void returns409WhenIsbnAlreadyExists() {
        Map<String, Object> first = validBook();
        createBook(first);
        Map<String, Object> second = validBook();
        second.put("isbn", first.get("isbn"));

        authorized().body(second).when().post().then().statusCode(409);
    }

    @Test
    void returns400WhenPriceIsNegative() {
        Map<String, Object> body = validBook();
        body.put("price", new BigDecimal("-0.01"));

        authorized().body(body).when().post().then().statusCode(400);
    }

    @Test
    void returns400WhenPriceIsMissing() {
        Map<String, Object> body = validBook();
        body.remove("price");

        authorized().body(body).when().post().then().statusCode(400);
    }

    @Test
    void acceptsPriceOfZero() {
        Map<String, Object> body = validBook();
        body.put("price", BigDecimal.ZERO);

        authorized().body(body).when().post().then().statusCode(201);
    }
}
