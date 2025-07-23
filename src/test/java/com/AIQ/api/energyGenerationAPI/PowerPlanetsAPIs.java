package com.AIQ.api.energyGenerationAPI;

import com.AIQ.api.RestResource;
import com.AIQ.utils.ConfigLoader;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static com.AIQ.api.Route.*;

public class PowerPlanetsAPIs {

    public static Response searchPowerPlanet(){

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("token", "Bearer " + ConfigLoader.getInstance().getToken());

        return RestResource.get(SEARCH, headers);
       }

    public static Response searchTopPowerPlanet(){
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("token", "Bearer " + ConfigLoader.getInstance().getToken());
        return RestResource.get(SEARCH_TOP, headers);
    }

    public static Response getSearchPercentage(String state){
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("token", "Bearer " + ConfigLoader.getInstance().getToken());
        return RestResource.getWithPathParam(SEARCH_PERCENTAGE, headers, "state", state);
    }

    public static Response getStates(){
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("token", "Bearer " + ConfigLoader.getInstance().getToken());
        return RestResource.get(STATES, headers);
    }

    public static Response getSearchState(String state){
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("token", "Bearer " + ConfigLoader.getInstance().getToken());
        return RestResource.getWithPathParam(SEARCH_STATE, headers, "state", state);
    }

    public static Response getID(){
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("token", "Bearer " + ConfigLoader.getInstance().getToken());
        return RestResource.get(ID, headers);
    }

}
