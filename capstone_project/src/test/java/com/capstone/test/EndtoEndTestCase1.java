package com.capstone.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.capstone.base.BaseTest;
import com.capstone.utilities.ScreenshotUtiles;

import java.time.Duration;

public class EndtoEndTestCase1 extends BaseTest {

    WebDriverWait wait;
    ExtentTest test;

    // Locators
    By link_Register = By.className("ico-register");
    By input_FirstName = By.id("FirstName");
    By input_LastName = By.id("LastName");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By input_ConfirmPassword = By.id("ConfirmPassword");
    By radio_Female = By.id("gender-female");
    By btn_Register = By.id("register-button");
    By link_Logout = By.className("ico-logout");
    By register_SuccessMsg = By.className("result");
    By loginButton = By.className("ico-login");
    By btn_Continue = By.xpath("//a[contains(@class, 'register-continue-button')]");

    @Test
    public void testRegisterAndLogout() throws Exception {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        test = extent.createTest("Verify Register and Logout Functionality");

        driver.get("https://demo.nopcommerce.com");

        // Verify homepage
        String expectedHomeTitle = "nopCommerce demo store";
        String actualHomeTitle = driver.getTitle();
        if (actualHomeTitle.contains(expectedHomeTitle)) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }

        // Click Register link
        driver.findElement(link_Register).click();

        // Verify URL contains "register"
        String currentURL = driver.getCurrentUrl();
        if (currentURL.contains("register")) {
            test.pass("Register page URL is correct");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_URL");
            test.fail("Register page URL is incorrect").addScreenCaptureFromPath(screenPath);
        }

        // Verify heading is "Register"
        WebElement heading = driver.findElement(By.tagName("h1"));
        if (heading.getText().equals("Register")) {
            test.pass("Register page heading is correct");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_Heading");
            test.fail("Register page heading is incorrect").addScreenCaptureFromPath(screenPath);
        }

        // Fill the registration form
        driver.findElement(radio_Female).click();
        driver.findElement(input_FirstName).sendKeys("Madana");
        driver.findElement(input_LastName).sendKeys("Aparna");

        // Generate unique email
        String uniqueEmail = "madanaaparna" + System.currentTimeMillis() + "@gmail.com";
        driver.findElement(input_Email).sendKeys(uniqueEmail);

        driver.findElement(input_Password).sendKeys("Aparna@123");
        driver.findElement(input_ConfirmPassword).sendKeys("Aparna@123");

        // Click Register
        driver.findElement(btn_Register).click();

        // Verify success or failure
        try {
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(register_SuccessMsg));
            if (successMsg.getText().contains("Your registration completed")) {
                test.pass("User registered successfully with email: " + uniqueEmail);
            }
        } catch (Exception e) {
            // Registration failed (email already exists or validation issue)
            WebElement errorMsg = driver.findElement(By.cssSelector(".message-error"));
            String screenPath = ScreenshotUtiles.Capture(driver, "Registration_Failed");
            test.fail("Registration failed: " + errorMsg.getText()).addScreenCaptureFromPath(screenPath);
            return; // stop further execution
        }

        // Click Continue
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(btn_Continue));
        continueButton.click();

        // Verify URL after clicking Continue
        String expectedURL = "https://demo.nopcommerce.com/";
        String actualURL = driver.getCurrentUrl();
        if (actualURL.equalsIgnoreCase(expectedURL)) {
            test.pass("Continue button redirected to homepage successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Continue_Redirection_Failed");
            test.fail("Continue button did not redirect correctly")
                .addScreenCaptureFromPath(screenPath)
                .info("Expected URL: " + expectedURL + " | Actual URL: " + actualURL);
        }

        // Logout
        driver.findElement(link_Logout).click();

        // Verify user is logged out by checking Login button
        if (driver.findElement(loginButton).isDisplayed()) {
            test.pass("Login button is visible after logout");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginButton_NotVisible");
            test.fail("Login button is not visible after logout").addScreenCaptureFromPath(screenPath);
        }
    }
}
