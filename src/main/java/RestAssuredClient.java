import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredClient {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }

    public RequestSpecification getAuthorizedSpec(String accessToken) {
        return new RequestSpecBuilder()
                .addHeader("Authorization", accessToken)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}
