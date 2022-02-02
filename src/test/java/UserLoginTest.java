import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Check user can login")
public class UserLoginTest {

    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new UserBuilder().random().buildUser();
        userClient.registerUser(user);
    }

    @Test
    public void checkUserCanLogin() {

        String actualResponseMessage = userClient.loginUser(UserCredentials.from(user))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("accessToken");

        assertNotNull("Returns empty access token in response", actualResponseMessage);
    }

    @Test
    public void checkErrorWhenUserLoginWithIncorrectPassword() {

        String incorrectPassword = new Faker().internet().password();
        String expectedResponseMessage = "email or password are incorrect";

        String actualResponseMessage = userClient.loginUser(new UserCredentials(user.getEmail(), incorrectPassword, user.getName()))
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorWhenUserLoginWithIncorrectEmail() {

        String incorrectEmail = new Faker().internet().emailAddress();
        String expectedResponseMessage = "email or password are incorrect";

        String actualResponseMessage = userClient.loginUser(new UserCredentials(incorrectEmail, user.getPassword(), user.getName()))
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorWhenUserLoginWithIncorrectName() {

        String incorrectName = new Faker().name().firstName();
        String expectedResponseMessage = "email or password are incorrect";

        String actualResponseMessage = userClient.loginUser(new UserCredentials(user.getEmail(), user.getPassword(), incorrectName))
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }
}
