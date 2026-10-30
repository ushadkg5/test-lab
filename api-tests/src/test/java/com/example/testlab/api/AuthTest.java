package com.example.testlab.api;

import org.junit.jupiter.api.Test;

class AuthTest extends ApiTestBase {

    @Test
    void postReturns401WhenTokenIsMissing() {
        anonymous().body(validBook()).when().post().then().statusCode(401);
    }

    @Test
    void postReturns401WhenTokenIsWrong() {
        anonymous().header("Authorization", "Bearer wrong-token")
                .body(validBook()).when().post().then().statusCode(401);
    }

    @Test
    void putReturns401WhenTokenIsMissing() {
        long id = createBook();

        anonymous().body(validBook()).when().put("/{id}", id).then().statusCode(401);
    }

    @Test
    void putReturns401WhenTokenIsWrong() {
        long id = createBook();

        anonymous().header("Authorization", "Bearer wrong-token")
                .body(validBook()).when().put("/{id}", id).then().statusCode(401);
    }

    @Test
    void deleteReturns401WhenTokenIsMissing() {
        long id = createBook();

        anonymous().when().delete("/{id}", id).then().statusCode(401);
    }

    @Test
    void deleteReturns401WhenTokenIsWrong() {
        long id = createBook();

        anonymous().header("Authorization", "Bearer wrong-token")
                .when().delete("/{id}", id).then().statusCode(401);
    }
}
