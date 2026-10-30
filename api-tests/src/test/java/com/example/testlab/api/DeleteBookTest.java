package com.example.testlab.api;

import org.junit.jupiter.api.Test;

class DeleteBookTest extends ApiTestBase {

    @Test
    void returns204AndBookIsGone() {
        long id = createBook();

        authorized().when().delete("/{id}", id).then().statusCode(204);

        anonymous().when().get("/{id}", id).then().statusCode(404);
    }

    @Test
    void returns404WhenIdIsUnknown() {
        authorized().when().delete("/{id}", UNKNOWN_ID).then().statusCode(404);
    }
}
