package com.AIQ.tests;

import com.AIQ.api.StatusCode;
import com.AIQ.api.energyGenerationAPI.PowerPlanetsAPIs;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetSearchAPITest extends BaseTest{

    @Test(description = "this is for search power planet")
    public void verifySearchPowerPlanetAPI(){
        Response response = PowerPlanetsAPIs.searchPowerPlanet();
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);

        // Parse the response into a List of Maps (objects)
        List<Map<String, Object>> powerPlants = response.jsonPath().getList("$");

        // Assert that the response is a non-empty array
        assertThat(powerPlants.isEmpty(), equalTo(false));

        // Assert that every element is an object (Map) with expected fields
        for (Map<String, Object> plant : powerPlants) {
            assertThat(plant.containsKey("id"), equalTo(true));
            assertThat(plant.containsKey("name"), equalTo(true));
            assertThat(plant.containsKey("state"), equalTo(true));
            assertThat(plant.containsKey("annual_net_generation"), equalTo(true));
            assertThat(plant.containsKey("latitude"), equalTo(true));
            assertThat(plant.containsKey("longitude"), equalTo(true));
        }


    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode){
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }
}
