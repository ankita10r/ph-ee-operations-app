package org.apache.fineract.test.cucumber.stepdef;

import com.google.gson.Gson;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.fineract.test.cucumber.Utils;

import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;

public class AuthStepDef extends BaseStepDef {

    public static final String TENANT_PARAM_NAME = "Platform-TenantId";
    public static final String DEFAULT_TENANT = "ibank-india";

    @When("I call the auth endpoint with username: {string} and password: {string}")
    public void authenticateWithUsernameAndPassword(String username, String password) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(BaseStepDef.tenant);
        requestSpec.header("Authorization", "Basic Y2xpZW50Og==");
        requestSpec.queryParam("username", username);
        requestSpec.queryParam("password", password);
        requestSpec.queryParam("grant_type", "password");

        BaseStepDef.response = RestAssured.given(requestSpec)
                .baseUri("https://ops-bk.sandbox.fynarfin.io")
                .body("{}")
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(200).build())
                .when()
                .post("/oauth/token")
                .andReturn().asString();
    }

    @Then("I should get a valid token")
    public void checkToken() {
        HashMap<String, Object> authResponse = new Gson().fromJson(BaseStepDef.response, HashMap.class);
        String token;
        try {
            token = (String) authResponse.get("access_token");
        } catch (Exception e) {
            token = null;
        }
        assertThat(token).isNotEmpty();
        BaseStepDef.accessToken = token;
        logger.info("Access token: " + BaseStepDef.accessToken);
    }

}