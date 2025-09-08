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
import java.util.List;

public class EndtoEndTestCase2 extends BaseTest {

    WebDriverWait wait;
    ExtentTest test;

    // Locators
    By link_Login = By.className("ico-login");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By btn_Login = By.cssSelector("button.login-button");
    By searchBox = By.id("small-searchterms");
    By btn_Search = By.cssSelector("button[type='submit']");
    By firstProduct_AddToCart = By.cssSelector(".product-item .product-box-add-to-cart-button");
    By link_Logout = By.className("ico-logout");
    By searchResults = By.cssSelector(".product-item"); // <-- Added for validation

    @Test
    public void testLoginSearchAddToCartAndLogout() throws Exception {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        test = extent.createTest("Verify Login → Search Product → Add To Cart → Logout Functionality");

        driver.get("https://demo.nopcommerce.com");

        // Verify homepage
        if (driver.getTitle().contains("nopCommerce demo store")) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }

        // Click Login
        driver.findElement(link_Login).click();

        // Enter credentials
        String email = "madanaaparna@gmail.com";
        String password = "Aparna";

        driver.findElement(input_Email).sendKeys(email);
        driver.findElement(input_Password).sendKeys(password);
        driver.findElement(btn_Login).click();

        // Verify successful login
        try {
            WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(link_Logout));
            if (logoutLink.isDisplayed()) {
                test.pass("User logged in successfully with email: " + email);
            }
        } catch (Exception e) {
            String screenPath = ScreenshotUtiles.Capture(driver, "Login_Failed");
            test.fail("Login failed with email: " + email).addScreenCaptureFromPath(screenPath);
            return;
        }

        // Search for product
        driver.findElement(searchBox).sendKeys("laptop");
        driver.findElement(btn_Search).click();

        // ✅ Corrected Search Verification
        try {
            List<WebElement> results = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResults));
            if (!results.isEmpty()) {
                test.pass("Search results displayed for product: laptop");
            } else {
                String screenPath = ScreenshotUtiles.Capture(driver, "Search_Failed");
                test.fail("Search results not displayed properly").addScreenCaptureFromPath(screenPath);
            }
        } catch (Exception e) {
            String screenPath = ScreenshotUtiles.Capture(driver, "Search_Failed_Exception");
            test.fail("Search results validation failed due to exception").addScreenCaptureFromPath(screenPath);
        }

        // Add first product to cart
        try {
            WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(firstProduct_AddToCart));
            addToCartBtn.click();
            test.pass("First product added to cart successfully");
        } catch (Exception e) {
            String screenPath = ScreenshotUtiles.Capture(driver, "AddToCart_Failed");
            test.fail("Failed to add product to cart").addScreenCaptureFromPath(screenPath);
        }

        // Logout
        driver.findElement(link_Logout).click();

        // Verify user logged out
        if (driver.findElement(link_Login).isDisplayed()) {
            test.pass("User logged out successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Logout_Failed");
            test.fail("Logout not successful").addScreenCaptureFromPath(screenPath);
        }
    }
}