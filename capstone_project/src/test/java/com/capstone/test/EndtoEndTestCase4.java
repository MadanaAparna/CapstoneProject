package com.capstone.test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.capstone.base.BaseTest;
import com.capstone.utilities.ScreenshotUtiles;

public class EndtoEndTestCase4 extends BaseTest {

    WebDriverWait wait;
    ExtentTest test;

    // 🔹 Locators (verified on nopCommerce demo)
    By link_Login = By.className("ico-login");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    // Corrected locator for the login button. Using a simpler CSS selector.
    By btn_Login = By.cssSelector("button.login-button"); 
    By link_Logout = By.className("ico-logout");
    By link_BooksCategory = By.linkText("Books");
    By btn_AddToWishlist = By.cssSelector(".product-item .add-to-wishlist-button");
    By link_Wishlist = By.className("ico-wishlist");
    By wishlistItem = By.cssSelector(".wishlist-content .product-name");
    By error_Login = By.cssSelector(".validation-summary-errors span");

    @Test
    public void testLoginAddToWishlistAndLogout() throws Exception {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        test = extent.createTest("Verify Login → Add to Wishlist → Verify Wishlist → Logout");

        // Step 1: Open Application
        driver.get("https://demo.nopcommerce.com");

        // Step 2: Verify homepage
        if (driver.getTitle().contains("nopCommerce demo store")) {
            test.pass("Home page opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page not loaded").addScreenCaptureFromPath(screenPath);
            return;
        }

        // Step 3: Click Login
        // Wait for the login link to be clickable before clicking it
        wait.until(ExpectedConditions.elementToBeClickable(link_Login)).click();
        test.info("Navigated to the login page");

        // Step 4: Enter credentials
        String email = "madanaaparna@gmail.com";
        String password = "Aparna";

        // Wait for input fields to be visible before interacting
        wait.until(ExpectedConditions.visibilityOfElementLocated(input_Email)).sendKeys(email);
        driver.findElement(input_Password).sendKeys(password);

        // Click the login button
        driver.findElement(btn_Login).click();

        // Step 5: Verify successful login or capture the error
        try {
            WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(link_Logout));
            if (logoutLink.isDisplayed()) {
                test.pass("User logged in successfully with email: " + email);
            }
        } catch (Exception e) {
            String loginError = "Unknown error";
            try {
                // Check for the specific "No customer account found" error message
                WebElement errorMessage = driver.findElement(error_Login);
                if (errorMessage.isDisplayed()) {
                    loginError = errorMessage.getText();
                }
            } catch (Exception ex) {
                loginError = "Login failed due to an unknown issue.";
            }

            String screenPath = ScreenshotUtiles.Capture(driver, "Login_Failed");
            test.fail("Login failed: " + loginError + " for email: " + email).addScreenCaptureFromPath(screenPath);
            return;
        }

        // Step 6: Navigate to Books category
        wait.until(ExpectedConditions.elementToBeClickable(link_BooksCategory)).click();
        test.info("Navigated to Books category");

        // Step 7: Add first product to Wishlist
        try {
            WebElement addToWishlistBtn = wait.until(ExpectedConditions.elementToBeClickable(btn_AddToWishlist));
            addToWishlistBtn.click();
            test.pass("Product added to Wishlist successfully");
        } catch (Exception e) {
            String screenPath = ScreenshotUtiles.Capture(driver, "AddToWishlist_Failed");
            test.fail("Failed to add product to Wishlist").addScreenCaptureFromPath(screenPath);
            return;
        }

        // Step 8: Navigate to Wishlist page
        wait.until(ExpectedConditions.elementToBeClickable(link_Wishlist)).click();

        // Step 9: Verify product is listed in Wishlist
        try {
            WebElement wishlistProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistItem));
            if (wishlistProduct.isDisplayed()) {
                test.pass("Wishlist product displayed: " + wishlistProduct.getText());
            }
        } catch (Exception e) {
            String screenPath = ScreenshotUtiles.Capture(driver, "Wishlist_Verification_Failed");
            test.fail("Product not found in Wishlist").addScreenCaptureFromPath(screenPath);
        }

        // Step 10: Logout
        wait.until(ExpectedConditions.elementToBeClickable(link_Logout)).click();
        if (wait.until(ExpectedConditions.visibilityOfElementLocated(link_Login)).isDisplayed()) {
            test.pass("User logged out successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Logout_Failed");
            test.fail("Logout failed").addScreenCaptureFromPath(screenPath);
        }
    }
}