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
 
public class EndToEndTestCase3 extends BaseTest {
 
    WebDriverWait wait;

    ExtentTest test;
 
    // Locators

    By link_Login = By.className("ico-login");

    By input_Email = By.id("Email");

    By input_Password = By.id("Password");

    By btn_Login = By.cssSelector("button.login-button");

    By featured_AddToCart = By.cssSelector(".product-item .product-box-add-to-cart-button"); // first featured product

    By cartLink = By.cssSelector("a.ico-cart");

    By cartProduct = By.cssSelector(".cart-item-row");  // product row in cart

    By link_Logout = By.className("ico-logout");
 
    @Test

    public void testAddFeaturedProductToCartAndVerify() throws Exception {

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        test = extent.createTest("Verify Add Featured Product → Navigate to Cart → Verify Item → Logout");
 
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
 
        // Enter credentials (use your own)

        String email = "madanaaparna@gmail.com";

        String password = "Aparna";
 
        driver.findElement(input_Email).sendKeys(email);

        driver.findElement(input_Password).sendKeys(password);

        driver.findElement(btn_Login).click();
 
        // Verify login success

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
 
        // Add Featured Product to Cart

        try {

            WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(featured_AddToCart));

            addToCartBtn.click();

            test.pass("Featured product added to cart successfully");

        } catch (Exception e) {

            String screenPath = ScreenshotUtiles.Capture(driver, "AddToCart_Failed");

            test.fail("Failed to add featured product to cart").addScreenCaptureFromPath(screenPath);

            return;

        }
 
        // Navigate to Cart

        driver.findElement(cartLink).click();
 
        // Verify product exists in Cart

        try {

            WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(cartProduct));

            if (cartItem.isDisplayed()) {

                test.pass("Product is listed in cart successfully");

            }

        } catch (Exception e) {

            String screenPath = ScreenshotUtiles.Capture(driver, "Cart_Verification_Failed");

            test.fail("Product not found in cart").addScreenCaptureFromPath(screenPath);

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