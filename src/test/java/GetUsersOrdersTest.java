import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.path.json.JsonPath.from;
import static org.junit.Assert.assertEquals;

@DisplayName("Check only authorized users can get orders info")
public class GetUsersOrdersTest {

    private OrderClient orderClient;
    private String accessToken;
    private int orderNumber;

    @Before
    public void setUp() {
        IngredientsClient ingredientsClient = new IngredientsClient();
        orderClient = new OrderClient();
        UserClient userClient = new UserClient();
        User user = new UserBuilder().random().buildUser();

        accessToken = userClient.registerUser(user)
                .then()
                .extract()
                .path("accessToken");

        orderNumber = orderClient.createOrder(ingredientsClient.createBurgerBody(), accessToken)
                .then()
                .extract()
                .path("order.number");
    }

    @Test
    public void checkAuthorizedUserCanGetHisOrders() {

        int actualOrderNumber = from(orderClient.getUsersOrders(accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString())
                .get("orders.findAll {it}.number[0]");

        assertEquals("Order number is incorrect", orderNumber, actualOrderNumber);

    }

    @Test
    public void checkUnauthorizedUserCannotGetOrders() {

        String expectedResponseMessage = "You should be authorised";

        String actualResponseMessage = orderClient.getUsersOrders()
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }
}
