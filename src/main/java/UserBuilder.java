import com.github.javafaker.Faker;

public class UserBuilder {

    private String email;
    private String password;
    private String name;

    public User buildUser() {
        return new User(email, password, name);
    }

    private String getRandomName() {
        return new Faker().name().firstName();
    }

    private String getRandomPassword() {
        return new Faker().internet().password();
    }

    private String getRandomEmail() {
        return new Faker().internet().emailAddress();
    }

    public UserBuilder random() {
        this.email = this.getRandomEmail();
        this.password = this.getRandomPassword();
        this.name = this.getRandomName();
        return this;
    }

    public UserBuilder random(boolean setEmail, boolean setPassword, boolean setName) {

        if (setEmail) {
            this.email = this.getRandomEmail();
        }

        if (setPassword) {
            this.password = this.getRandomPassword();
        }

        if (setName) {
            this.name = this.getRandomName();
        }

        return this;
    }
}
