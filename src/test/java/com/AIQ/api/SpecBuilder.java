package com.AIQ.api;

import com.AIQ.utils.ConfigLoader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilder {

    public static RequestSpecification getRequestSpec(){
        return new RequestSpecBuilder().
                setBaseUri(ConfigLoader.getInstance().getBaseUrl()).
//                setBasePath(AUTH).
                setContentType(ContentType.JSON).
                addFilter(new AllureRestAssured()).
                log(LogDetail.ALL).
                build();
    }

    public static RequestSpecification getAnotherRequestSpec(){
        return new RequestSpecBuilder().
                setBaseUri(System.getProperty("ANOTHER_BASE_URI")).
    //            setBaseUri("another base url").
                setContentType(ContentType.URLENC).
                addFilter(new AllureRestAssured()).
                log(LogDetail.ALL).
                build();
    }

    public static ResponseSpecification getResponseSpec(){
        return new ResponseSpecBuilder().
                log(LogDetail.ALL).
                build();
    }
}
