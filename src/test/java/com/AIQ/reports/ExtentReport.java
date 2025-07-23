package com.AIQ.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentReport {
    static ExtentReports extent;
    final static String filePath = "Extent.html";
//    static Map<Integer, ExtentTest> extentTestMap = new HashMap();

    static Map<Integer, ExtentTest> extentTestMap = new ConcurrentHashMap<>();  // Use ConcurrentHashMap


    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
        	ExtentSparkReporter html = new ExtentSparkReporter("Extent.html");
        	html.config().setDocumentTitle("Fodel APIs Framework");
        	html.config().setReportName("Fodel App");
        	html.config().setTheme(Theme.DARK);
            extent = new ExtentReports();
            extent.attachReporter(html);
        }

        return extent;
    }

//    public static synchronized ExtentTest getTest() {
//        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
//    }
//
//    public static synchronized ExtentTest startTest(String testName, String desc) {
//        ExtentTest test = getReporter().createTest(testName, desc);
//        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
//        return test;
//    }

//    public static Map<Integer, ExtentTest> getExtentTestMap() {
//        return extentTestMap;
//    }

//    public static synchronized ExtentTest getTest() {
//        return extentTestMap.get((int) (long) Thread.currentThread().getId());
//    }
//
//    public static synchronized void setTest(ExtentTest test) {
//        extentTestMap.put((int) (long) Thread.currentThread().getId(), test);
//    }
//
//    public static ExtentTest startTest(String testName, String description) {
//        ExtentTest test = getReporter().createTest(testName, description);
//        setTest(test);  // Store the test for the current thread
//        return test;
//    }

    // Set the test instance in the map
    public static synchronized void setTest(ExtentTest test) {
        extentTestMap.put((int) Thread.currentThread().getId(), test);  // Cast long to int
    }

    // Get the test instance from the map
    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());  // Cast long to int
    }

    // Get the size of the test map
    public static synchronized int getTestMapSize() {
        return extentTestMap.size();
    }

    public static ExtentTest startTest(String testName, String description) {
        ExtentTest test = getReporter().createTest(testName, description);
        setTest(test);  // Store the test for the current thread
        return test;
    }

    private static String generateSummary(int passedTests, int failedTests, int skippedTests) {
        int totalTests = passedTests + failedTests + skippedTests;  // Calculate total tests
        StringBuilder summary = new StringBuilder();
        summary.append("<h2>Summary</h2>");
        summary.append("<p>Total Tests: ").append(totalTests).append("</p>");
        summary.append("<p>Passed: ").append(passedTests).append("</p>");
        summary.append("<p>Failed: ").append(failedTests).append("</p>");
        summary.append("<p>Skipped: ").append(skippedTests).append("</p>");
        return summary.toString();
    }

    public static void exportTestSummary(String filePath, int passedTests, int failedTests, int skippedTests) throws IOException {
        int totalTests = passedTests + failedTests + skippedTests;

//        // Capture current date and time
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
//        String formattedDateTime = currentDateTime.format(formatter);
        // Capture current date and time in Dubai time zone
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime dubaiDateTime = localDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("Asia/Dubai"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        String formattedDateTime = dubaiDateTime.format(formatter);

        // Writing the summary to a text file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.format("execution_date=%s\n", formattedDateTime));
            writer.write(String.format("total_tests=%d\n", totalTests));
            writer.write(String.format("passed=%d\n", passedTests));
            writer.write(String.format("failed=%d\n", failedTests));
            writer.write(String.format("skipped=%d\n", skippedTests));
        } catch (IOException e) {
            System.err.println("Error writing test summary: " + e.getMessage());
            throw e;
        }

        System.out.println("Test summary exported successfully to: " + filePath);
    }

    public static void generateHtmlReport(String outputPath, int passedTests, int failedTests, int skippedTests) throws IOException {
        StringBuilder reportContent = new StringBuilder();

        reportContent.append("<html><head><title>Test Report</title></head><body>");
        reportContent.append("<h1>Test Automation Report</h1>");
        reportContent.append("<h2>Run Date: " + LocalDate.now() + "</h2>");

        String summary = generateSummary(passedTests, failedTests, skippedTests);  // Pass counts
        reportContent.append(summary);

        reportContent.append("<h2>Test Case Details</h2>");
        reportContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        reportContent.append("<tr><th>Test Name</th><th>Status</th></tr>");

        extent.getReport().getTestList().forEach(test -> {
            reportContent.append(String.format("<tr><td>%s</td><td>%s</td></tr>",
                    test.getName(), test.getStatus()));
        });

        reportContent.append("</table>");
        reportContent.append("</body></html>");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(reportContent.toString());
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
            throw e;  // Rethrow to handle upstream if needed
        }

        System.out.println("Report generated successfully at: " + outputPath);
    }
}
