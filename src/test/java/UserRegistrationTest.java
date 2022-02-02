import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Check the user can register")
public class UserRegistrationTest {

    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new UserBuilder().random().buildUser();
    }

    @Test
    public void checkUserCanRegister() {
        String actualResponse = userClient.registerUser(user)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("accessToken");

        assertNotNull("Returned empty access token in response", actualResponse);
    }

    @Test
    public void checkErrorWhenTryingToRegisterExistingUser() {

        String expectedResponseMessage = "User already exists";

        userClient.registerUser(user);

        String actualResponseMessage = userClient.registerUser(user)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .path("message");

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }
}
