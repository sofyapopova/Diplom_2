import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "/api/orders";

    @Step("Order creation without accessToken")
    public Response createOrder(Map<String, List<String>> ingredientsIds) {
        return given()
                .spec(getBaseSpec())
                .body(ingredientsIds)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Order creation with accessToken")
    public Response createOrder(Map<String, List<String>> ingredientsIds, String accessToken) {
        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(ingredientsIds)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Get orders of authorized user")
    public Response getUsersOrders(String accessToken) {
        return given()
                .spec(getAuthorizedSpec(accessToken))
                .when()
                .get(ORDER_PATH);
    }

    @Step("Get orders of unauthorized user")
    public Response getUsersOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH);
    }
}
