import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

@DisplayName("Check order creation cases")
public class OrderCreationTest {

    private IngredientsClient ingredientsClient;
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        ingredientsClient = new IngredientsClient();
        orderClient = new OrderClient();
        UserClient userClient = new UserClient();
        User user = new UserBuilder().random().buildUser();
        accessToken = userClient.registerUser(user)
                .then()
                .extract()
                .path("accessToken");
    }

    @Test
    public void checkUnauthorizedUserCannotCreateOrder() {

        orderClient.createOrder(ingredientsClient.createBurgerBody())
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Test
    public void checkAuthorizedUserCanCreateOrder() {

        orderClient.createOrder(ingredientsClient.createBurgerBody(), accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .body("order.number", notNullValue());
    }

    @Test
    public void checkOrderCannotBeCreatedWithoutIngredients() {

        String expectedResponseMessage = "Ingredient ids must be provided";

        orderClient.createOrder(ingredientsClient.createEmptyBurgerBody(), accessToken)
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo(expectedResponseMessage));
    }

    @Test
    public void checkOrderCannotBeCreatedWithIncorrectIngredientsId() {

        String expectedResponseMessage = "One or more ids provided are incorrect";

        orderClient.createOrder(ingredientsClient.createIncorrectBurgerBody(), accessToken)
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo(expectedResponseMessage));
    }
}
