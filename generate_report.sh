#!/bin/bash

# Capture the test suite type and Allure report URL from the arguments
TEST_SUITE=$1
ALLURE_REPORT_URL=$2

# Debug: Print input values to verify they're passed correctly
echo "DEBUG: TEST_SUITE=$TEST_SUITE"
echo "DEBUG: ALLURE_REPORT_URL=$ALLURE_REPORT_URL"

# Define paths
TEMPLATE_PATH="reports/reportTemplate.html"  # Path to the report template
SUMMARY_PATH="reports/test_summary.txt"      # Path to the test summary file
FINAL_REPORT_PATH="reports/final_report.html" # Path to the final generated report

# Validate numeric values
validate_number() {
    local value=$1
    local name=$2
    if ! [[ "$value" =~ ^[0-9]+$ ]]; then
        echo "ERROR: Invalid $name value: $value"
        exit 1
    fi
}

# Calculate percentage safely
calculate_percentage() {
    local part=$1
    local total=$2
    if [ "$total" -eq 0 ]; then
        echo 0
    else
        echo $(( (part * 100) / total ))
    fi
}

# Initialize variables for summary values
TOTAL_TESTS=0
PASSED=0
FAILED=0
SKIPPED=0
EXECUTION_DATE=""

# Read the summary file and extract values
if [[ -f $SUMMARY_PATH ]]; then
    echo "Reading summary from $SUMMARY_PATH"
    while IFS='=' read -r key value; do
        case $key in
            total_tests)
                TOTAL_TESTS="$value"
                ;;
            passed)
                PASSED="$value"
                ;;
            failed)
                FAILED="$value"
                ;;
            skipped)
                SKIPPED="$value"
                ;;
            execution_date)
                EXECUTION_DATE="$value"
                ;;
        esac
    done < "$SUMMARY_PATH"

    # Validate numeric values
    validate_number "$TOTAL_TESTS" "total_tests"
    validate_number "$PASSED" "passed"
    validate_number "$FAILED" "failed"
    validate_number "$SKIPPED" "skipped"

    # Calculate pass, fail, and skip rates
    PASS_RATE=$(calculate_percentage "$PASSED" "$TOTAL_TESTS")
    FAIL_RATE=$(calculate_percentage "$FAILED" "$TOTAL_TESTS")
    SKIP_RATE=$(calculate_percentage "$SKIPPED" "$TOTAL_TESTS")

    # If SKIPPED is 0, set SKIP_RATE to 0 explicitly
    if [[ "$SKIPPED" -eq 0 ]]; then
        SKIP_RATE=0
    fi

    # If EXECUTION_DATE is empty, set it to the current date/time
    if [[ -z $EXECUTION_DATE ]]; then
        EXECUTION_DATE=$(date +"%Y-%m-%d %I:%M %p")
    fi
else
    echo "ERROR: Summary file not found at $SUMMARY_PATH"
    exit 1
fi

# Replace placeholders in the final report
if [[ -f $TEMPLATE_PATH ]]; then
    cp "$TEMPLATE_PATH" "$FINAL_REPORT_PATH"

    # Escape special characters in the URL
    ESCAPED_URL=$(printf '%s\n' "$ALLURE_REPORT_URL" | sed 's:[\/&]:\\&:g;$!s/$/\\/')

    # Replace placeholders using perl for better handling of special characters
    perl -i -pe "s/\{\{TEST_SUITE\}\}/$TEST_SUITE/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{TOTAL_TESTS\}\}/$TOTAL_TESTS/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{PASSED\}\}/$PASSED/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{FAILED\}\}/$FAILED/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{SKIPPED\}\}/$SKIPPED/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{EXECUTION_DATE\}\}/$EXECUTION_DATE/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s|\{\{ALLURE_REPORT_URL\}\}|$ESCAPED_URL|g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{PASS_RATE\}\}/$PASS_RATE/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{FAIL_RATE\}\}/$FAIL_RATE/g" "$FINAL_REPORT_PATH"
    perl -i -pe "s/\{\{SKIP_RATE\}\}/$SKIP_RATE/g" "$FINAL_REPORT_PATH"

    echo "✅ Final report generated successfully at: $FINAL_REPORT_PATH"

    # Debug: Verify if the replacement worked
    if grep -q "{{ALLURE_REPORT_URL}}" "$FINAL_REPORT_PATH"; then
        echo "❌ URL placeholder not replaced!"
        echo "Content around placeholder:"
        grep -A 2 -B 2 "{{ALLURE_REPORT_URL}}" "$FINAL_REPORT_PATH"
    else
        echo "✅ URL placeholder replaced successfully!"
    fi
else
    echo "ERROR: Report template not found at $TEMPLATE_PATH"
    exit 1
fi