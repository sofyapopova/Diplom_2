import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

public class IngredientsClient extends RestAssuredClient {

    private static final String INGREDIENTS_PATH = "/api/ingredients";
    private static final String INGREDIENTS_KEY = "ingredients";

    private final String bunId;
    private final String fillingId;

    public IngredientsClient() {

        JsonPath ingredientsJson = getIngredientsJson();

        bunId = ingredientsJson.get("data.findAll { it.type == \"bun\" }._id[0]");
        fillingId = ingredientsJson.get("data.findAll { it.type == \"main\" }._id[0]");
    }

    @Step("Get ingredients")
    private JsonPath getIngredientsJson() {
        return from(given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .asString());
    }

    @Step("Get incorrect ingredient id")
    private String reverseIngredientId(String ingredientId) {

        return new StringBuilder(ingredientId).reverse().toString();
    }

    @Step("Create correct burger body")
    public Map<String, List<String>> createBurgerBody() {

        List<String> burger = List.of(bunId, fillingId, bunId);

        Map<String, List<String>> burgerBody = new HashMap<>();
        burgerBody.put(INGREDIENTS_KEY, burger);

        return burgerBody;
    }

    @Step("Create empty burger body")
    public Map<String, List<String>> createEmptyBurgerBody() {

        Map<String, List<String>> emptyBurgerBody = new HashMap<>();
        emptyBurgerBody.put(INGREDIENTS_KEY, new ArrayList<>());

        return emptyBurgerBody;
    }

    @Step("Create incorrect burger body")
    public Map<String, List<String>> createIncorrectBurgerBody() {

        String bun = reverseIngredientId(bunId);
        String filling = reverseIngredientId(fillingId);

        List<String> burger = List.of(bun, filling, bun);

        Map<String, List<String>> incorrectBurgerBody = new HashMap<>();
        incorrectBurgerBody.put(INGREDIENTS_KEY, burger);

        return incorrectBurgerBody;
    }
}
