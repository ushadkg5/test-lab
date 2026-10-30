package com.example.testlab.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class UpdateBookTest extends ApiTestBase {

    @Test
    void returns200AndStoresChanges() {
        long id = createBook();
        Map<String, Object> changes = validBook();
        changes.put("title", "Dune Messiah");

        authorized().body(changes).when().put("/{id}", id).then().statusCode(200);

        String title = anonymous().when().get("/{id}", id).then().statusCode(200)
                .extract().jsonPath().getString("title");
        assertThat(title).isEqualTo("Dune Messiah");
    }

    @Test
    void returns404WhenIdIsUnknown() {
        authorized().body(validBook()).when().put("/{id}", UNKNOWN_ID).then().statusCode(404);
    }

    @Test
    void returns409WhenIsbnBelongsToAnotherBook() {
        Map<String, Object> other = validBook();
        createBook(other);
        long id = createBook();
        Map<String, Object> changes = validBook();
        changes.put("isbn", other.get("isbn"));

        authorized().body(changes).when().put("/{id}", id).then().statusCode(409);
    }
}
