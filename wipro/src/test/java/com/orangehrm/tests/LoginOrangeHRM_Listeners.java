package com.orangehrm.tests;

import java.io.IOException;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.utilities.ExcelUtiles;
import com.orangehrm.utilities.Screenshot;

import pack1.login_pagefactory;

@Listeners(com.orangehrm.listeners.TestListeners.class)
public class LoginOrangeHRM_Listeners extends BaseTest {

    static String projectpath = System.getProperty("user.dir");

    @Test(dataProvider = "logindata")
    public void verifylogin(String username, String password) throws IOException {
        navigateurl("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        login_pagefactory obj = PageFactory.initElements(getDriver(), login_pagefactory.class);
        obj.enterusername(username);
        obj.enterpassword(password);
        obj.clickonlogin();

        String actualtitle = getDriver().getTitle();
        String safeUsername = username.replaceAll("[^a-zA-Z0-9]", "_");

        if (actualtitle.equalsIgnoreCase("OrangeHR")) {
            getTest().pass("Login Successful and title is matched: " + actualtitle);
        } else {
            String screenpath = Screenshot.Capture(getDriver(), "Login_Failed_" + safeUsername);
            getTest().fail("Login Unsuccessful and title is not matched: " + actualtitle)
                    .addScreenCaptureFromPath(screenpath);
        }
    }

    @Test
    public void question1_google() throws IOException {
        navigateurl("https://www.google.com/");
        String actualtitle = getDriver().getTitle();

        if (actualtitle.equalsIgnoreCase("Google")) {
            getTest().pass("Google title is matched");
        } else {
            String screenpath = Screenshot.Capture(getDriver(), "Google_Title_Mismatched");
            getTest().fail("Google title is not matched: " + actualtitle)
                    .addScreenCaptureFromPath(screenpath);
        }
    }

    @DataProvider
    public Object[][] logindata() throws IOException {
        String excelpath = projectpath + "\\src\\test\\resources\\Testdata\\data.xlsx";
        return ExcelUtiles.getdata(excelpath, "Sheet1");
    }
}
