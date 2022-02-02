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
                .when()
                .post(USER_PATH + "/register");
    }

    @Step("Login user")
    public Response loginUser(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "/login");
    }

    @Step("Update user data with accessToken")
    public Response updateUserData(UserCredentials credentials, String accessToken) {
        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(credentials)
                .when()
                .patch(USER_PATH + "/user");
    }

    @Step("Update user data without accessToken")
    public Response updateUserData(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .patch(USER_PATH + "/user");
    }
}
