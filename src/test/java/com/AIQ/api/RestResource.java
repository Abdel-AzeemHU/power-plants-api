package com.AIQ.api;

import io.restassured.response.Response;

import java.util.Map;

import static com.AIQ.api.SpecBuilder.getRequestSpec;
import static com.AIQ.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response postWithBody(String path, Object requestAuth){
        return given(getRequestSpec()).
                body(requestAuth).
        when().post(path).
        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response get(String path, Map<String, String> headers){
        return given(getRequestSpec()).
                headers(headers).
        when().get(path).
        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response getWithPathParam(String path, Map<String, String> headers, String paramName, String paramValue) {
        String resolvedPath = buildPathWithParam(path, paramName, paramValue);
        return given(getRequestSpec()).
                headers(headers).
                when().get(resolvedPath).
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static String buildPathWithParam(String path, String paramName, String paramValue) {
        return path.replace("{" + paramName + "}", paramValue);
    }

}
