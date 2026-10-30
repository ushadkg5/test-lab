package com.example.testlab.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReadBookTest extends ApiTestBase {

    @Test
    void returnsBookByIdWithoutToken() {
        Map<String, Object> body = validBook();
        long id = createBook(body);

        String isbn = anonymous().when().get("/{id}", id).then().statusCode(200)
                .extract().jsonPath().getString("isbn");

        assertThat(isbn).isEqualTo(body.get("isbn"));
    }

    @Test
    void listsBooksWithoutToken() {
        long id = createBook();

        List<Long> ids = anonymous().when().get().then().statusCode(200)
                .extract().jsonPath().getList("id", Long.class);

        assertThat(ids).contains(id);
    }

    @Test
    void returns404WhenIdIsUnknown() {
        anonymous().when().get("/{id}", UNKNOWN_ID).then().statusCode(404);
    }
}
