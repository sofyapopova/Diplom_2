import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@DisplayName("Check the authorized user can change his data")
@RunWith(Parameterized.class)

public class UpdateUserDataParameterizedTest {

    private UserClient userClient;
    private User user = new UserBuilder().random().buildUser();

    private UserCredentials userCredentials;

    public UpdateUserDataParameterizedTest(String email, String password, String name) {

        userCredentials = UserCredentials.from(user);

        if (email != null) {
            if (email.equals("null")) {
                userCredentials.setEmail(null);
            } else {
                userCredentials.setEmail(email);
            }
        }

        if (password != null) {
            if (password.equals("null")) {
                userCredentials.setPassword(null);
            } else {
                userCredentials.setPassword(password);
            }
        }

        if (name != null) {
            if (name.equals("null")) {
                userCredentials.setName(null);
            } else {
                userCredentials.setName(name);
            }
        }
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        String randomEmail = new Faker().internet().emailAddress();
        String randomPassword = new Faker().internet().password();
        String randomName = new Faker().name().firstName();
        return new Object[][]{
                {null, null, null},
                {null, null, randomName},
                {null, null, ""},
                {null, null, "null"},
                {null, randomPassword, null},
                {null, "", null},
                {null, "null", null},
                {randomEmail, null, null},
                {"", null, null},
                {"", "", ""},
                {"null", null, null},
                {randomEmail, "null", "null"},
                {"null", randomPassword, "null"},
                {"null", "null", randomName},
                {"null", "null", "null"}
        };
    }

    @Test
    public void checkAuthorizedUserCanUpdateData() {

        String accessToken = userClient.registerUser(user)
                .then()
                .extract()
                .path("accessToken");

        ValidatableResponse actualResponseMessage = userClient.updateUserData(userCredentials, accessToken)
                .then()
                .assertThat()
                .statusCode(200);

        String actualUserEmail = actualResponseMessage
                .extract()
                .path("user.email");

        String actualUserName = actualResponseMessage
                .extract()
                .path("user.name");

        String expectedUserEmail = userCredentials.email;
        String expectedUserName = userCredentials.name;

        assertEquals("New user email is incorrect", expectedUserEmail, actualUserEmail);
        assertEquals("New user name is incorrect", expectedUserName, actualUserName);
    }
}
