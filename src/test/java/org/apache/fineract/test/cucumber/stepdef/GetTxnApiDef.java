package org.apache.fineract.test.cucumber.stepdef;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.fineract.test.cucumber.Utils;


import static com.google.common.truth.Truth.assertThat;

public class GetTxnApiDef extends BaseStepDef {


    @Given("I have tenant as {string}")
    public void setTenant(String tenant) {
        BaseStepDef.tenant = tenant;
        assertThat(BaseStepDef.tenant).isNotEmpty();
    }

    @When("I call the get txn summary API with expected status of {int}")
    public void callTxnReqApi(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(BaseStepDef.tenant);
        requestSpec.header("Authorization", "Bearer " + BaseStepDef.accessToken);
        requestSpec.queryParam("batchId", BaseStepDef.batchId);

        BaseStepDef.response = RestAssured.given(requestSpec)
                .baseUri("https://ops-bk.sandbox.fynarfin.io")
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when()
                .get("/api/v1/transactionRequests")
                .andReturn().asString();

        logger.info("Batch Summary Response: " + BaseStepDef.response);
    }


    @Then("I should get non empty response")
    public void nonEmptyResponseCheck() {
        assertThat(BaseStepDef.response).isNotNull();
    }


}