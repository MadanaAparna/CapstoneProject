package com.orangehrm.listeners;

import java.io.IOException;

import org.testng.*;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.orangehrm.base.BaseTest;

public class TestListeners extends BaseTest implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = getExtent().createTest(result.getMethod().getMethodName());
        setTest(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getTest().log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());

        try {
            String screenshotpath = Screenshot.Capture(getDriver(), result.getMethod().getMethodName());
            getTest().addScreenCaptureFromPath(screenshotpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=========== Test Suite Started ========" + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("=========== Test Suite Finished ========" + context.getName());
    }
}
