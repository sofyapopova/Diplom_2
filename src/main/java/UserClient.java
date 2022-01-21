import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private static final String USER_PATH = "/api/auth";

    @Step("User registration")
    public Response registerUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log()
                .all()
                .when()
                .post(USER_PATH + "/register");
    }
}
