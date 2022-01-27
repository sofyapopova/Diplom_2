import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

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

        orderClient.getUsersOrders(accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .body("orders.findAll {it}.number[0]", equalTo(orderNumber));
    }

    @Test
    public void checkUnauthorizedUserCannotGetOrders() {

        String expectedResponseMessage = "You should be authorised";

        orderClient.getUsersOrders()
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo(expectedResponseMessage));
    }

}
