public class UserCredentials {

    public final String email;
    public final String password;
    public final String name;

    public UserCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.email, user.password, user.name);
    }
}
