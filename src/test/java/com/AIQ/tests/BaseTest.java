package com.AIQ.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class BaseTest {

    @BeforeMethod
    public void beforeMethod(Method m){
        System.out.println("STARTING TEST: " + m.getName());
        System.out.println("THREAD ID: " + Thread.currentThread().getId());
    }

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Starting Test Suite: " + this.getClass().getSimpleName());
        // Additional setup can be done here if needed

    }
}
