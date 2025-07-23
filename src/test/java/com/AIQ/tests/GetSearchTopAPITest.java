package com.AIQ.tests;

import com.AIQ.api.StatusCode;
import com.AIQ.api.energyGenerationAPI.PowerPlanetsAPIs;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetSearchTopAPITest extends BaseTest {


    @Test(description = "this is for search power planet")
    public void verifySearchTopPowerPlanetAPI(){
        Response response = PowerPlanetsAPIs.searchTopPowerPlanet();
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode){
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }

}
