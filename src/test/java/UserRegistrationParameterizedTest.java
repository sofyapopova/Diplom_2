import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@DisplayName("Check the user cannot register without the required fields")
@RunWith(Parameterized.class)
public class UserRegistrationParameterizedTest {

    private final boolean setEmail;
    private final boolean setPassword;
    private final boolean setName;

    private UserClient userClient;
    private User user;

    public UserRegistrationParameterizedTest(boolean setEmail, boolean setPassword, boolean setName) {
        this.setEmail = setEmail;
        this.setPassword = setPassword;
        this.setName = setName;
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new UserBuilder().random(setEmail, setPassword, setName).buildUser();
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        return new Object[][]{
                {false, false, false},
                {false, false, true},
                {false, true, false},
                {false, true, true},
                {true, false, false},
                {true, false, true},
                {true, true, false}
        };
    }

    @Test
    public void checkUserCannotRegisterWithoutRequiredFields() {

        String expectedResponseMessage = "Email, password and name are required fields";

        String actualResponseMessage = userClient.registerUser(user)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .path("message");

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

}
