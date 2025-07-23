package com.AIQ.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.AIQ.reports.ExtentReport;
import com.AIQ.tests.BaseTest;
import com.AIQ.utils.TestUtils;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener, ISuiteListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<String, String> suiteDetails = new HashMap<>();

    TestUtils utils = new TestUtils();
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static int skippedTests = 0;

    @Override
    public void onTestStart(ITestResult result) {
        BaseTest base = new BaseTest();
        ExtentTest test = ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
                .assignAuthor("Abdelazeem");

        // Store the test instance for use during the test
        ExtentReport.setTest(test);
        System.out.println("Test started: " + result.getName());
        System.out.println("Starting Test: " + result.getName() + " | Total tests in Extent Map: " + ExtentReport.getTestMapSize());  // Add this
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReport.getTest();  // Retrieve the current test
        if (test != null) {
            test.log(Status.PASS, "Test Passed");
            passedTests++;  // Increment passed tests
        }
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Log the exception stack trace
        if (result.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
            utils.log().error(sw.toString());

            // Capture the failure in the Extent report
            ExtentTest test = ExtentReport.getTest();
            if (test != null) {
                // Attach the failure message to the report
                test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());

                // Optionally, capture a screenshot or other relevant data
                // String base64Image = getBase64Screenshot(); // Uncomment this if you want to add screenshots
                // test.fail("Failure Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
            }
        }
        failedTests++;  // Increment failed tests
        System.out.println("Test Failed: " + result.getName());
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReport.getTest();  // Retrieve the current test
        if (test != null) {
            test.log(Status.SKIP, "Test Skipped");
            skippedTests++;  // Increment skipped tests
        }
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Implement if needed
    }

    @Override
    public void onStart(ITestContext context) {
        // Called when the test starts
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush the Extent report
        ExtentReport.getReporter().flush();
        // Generate reports
        try {
            ExtentReport.generateHtmlReport("reports/final_report.html", passedTests, failedTests, skippedTests); // Generate a detailed report
            ExtentReport.exportTestSummary("reports/test_summary.txt", passedTests, failedTests, skippedTests); // Export test summary
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("All tests completed. HTML report generated.");
    }
}