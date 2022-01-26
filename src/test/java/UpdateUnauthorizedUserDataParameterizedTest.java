import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@DisplayName("Check the unauthorized user cannot change his data")
@RunWith(Parameterized.class)

public class UpdateUnauthorizedUserDataParameterizedTest {

    private UserClient userClient;
    private User user = new UserBuilder().random().buildUser();

    private UserCredentials userCredentials;

    public UpdateUnauthorizedUserDataParameterizedTest(String email, String password, String name) {

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
        userClient.registerUser(user);
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
    public void checkUnauthorizedUserCannotUpdateData() {

        String actualResponseMessage = userClient.updateUserData(userCredentials)
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");

        String expectedResponseMessage = "You should be authorised";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }
}
