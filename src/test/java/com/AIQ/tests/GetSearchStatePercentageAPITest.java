package com.AIQ.tests;

import com.AIQ.api.StatusCode;
import com.AIQ.api.energyGenerationAPI.PowerPlanetsAPIs;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetSearchStatePercentageAPITest extends BaseTest {

    @Test(description = "Get state percentage by state code")
    public void verifySearchStatePercentageAPI(){

        String state = "AK";
        Response response = PowerPlanetsAPIs.getSearchPercentage(state);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode){
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }
}
