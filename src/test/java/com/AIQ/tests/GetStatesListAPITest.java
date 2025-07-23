package com.AIQ.tests;

import com.AIQ.api.StatusCode;
import com.AIQ.api.energyGenerationAPI.PowerPlanetsAPIs;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetStatesListAPITest extends BaseTest{

    @Test(description = "Get list of states")
    public void verifyGetStateListAPI(){
        Response response = PowerPlanetsAPIs.getStates();
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);

        // Get the response as a list of strings
        List<String> states = response.jsonPath().getList("$");

        // Assert that it's not empty (which implies it's an array)
        assertThat(states.isEmpty(), equalTo(false));

        // Optional: Assert that all elements are strings
        for (String state : states) {
            assertThat(state instanceof String, equalTo(true));
        }
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode){
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }
}
