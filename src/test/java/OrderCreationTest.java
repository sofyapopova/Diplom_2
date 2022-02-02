import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

        boolean isOrderCreated = orderClient.createOrder(ingredientsClient.createBurgerBody())
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("success");

        assertFalse("Unauthorized user can create order", isOrderCreated);
    }

    @Test
    public void checkAuthorizedUserCanCreateOrder() {

        int orderNumber = orderClient.createOrder(ingredientsClient.createBurgerBody(), accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("order.number");

        assertNotNull("Authorized user can't create order", orderNumber);
    }

    @Test
    public void checkOrderCannotBeCreatedWithoutIngredients() {

        String expectedResponseMessage = "Ingredient ids must be provided";

        String actualResponseMessage = orderClient.createOrder(ingredientsClient.createEmptyBurgerBody(), accessToken)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertEquals("Order can be created without ingredients", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkOrderCannotBeCreatedWithIncorrectIngredientsId() {

        String expectedResponseMessage = "One or more ids provided are incorrect";

        String actualResponseMessage = orderClient.createOrder(ingredientsClient.createIncorrectBurgerBody(), accessToken)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertEquals("Order can be created with incorrect ingredients id", expectedResponseMessage, actualResponseMessage);
    }
}
