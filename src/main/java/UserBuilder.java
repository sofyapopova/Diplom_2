import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class UserBuilder {

    private String email;
    private String password;
    private String name;

    public User buildUser() {
        return new User(email, password, name);
    }

    private String getRandomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String getRandomEmail() {
        return new Faker().internet().emailAddress();
    }

    public UserBuilder random() {
        this.email = this.getRandomEmail();
        this.password = this.getRandomString();
        this.name = this.getRandomString();
        return this;
    }

    public UserBuilder random(boolean setEmail, boolean setPassword, boolean setName) {

        if (setEmail) {
            this.email = this.getRandomEmail();
        }

        if (setPassword) {
            this.password = this.getRandomString();
        }

        if (setName) {
            this.name = this.getRandomString();
        }

        return this;
    }
}
