package com.capstone.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.UnhandledAlertException;

import com.aventstack.extentreports.ExtentTest;
import com.capstone.base.BaseTest;
import com.capstone.utilities.ScreenshotUtiles;

import java.time.Duration;

public class EndtoEndTestCase5 extends BaseTest {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;

    // Locators
    By loginLink = By.className("ico-login");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By btn_Login = By.xpath("//button[contains(@class,'login-button')]");
    By logoutLink = By.className("ico-logout");
    By loginButton = By.className("ico-login");
    By cartIcon = By.xpath("//span[@class='cart-label']");
    By notificationBar = By.id("bar-notification");
    By agreeCheckbox = By.id("termsofservice");
    By checkoutBtn = By.id("checkout");

    // Billing Address Locators
    By billingFirstName = By.id("BillingNewAddress_FirstName");
    By billingLastName = By.id("BillingNewAddress_LastName");
    By billingEmail = By.id("BillingNewAddress_Email");
    By billingCountry = By.id("BillingNewAddress_CountryId");
    By billingState = By.id("BillingNewAddress_StateProvinceId");
    By billingCity = By.id("BillingNewAddress_City");
    By billingAddress1 = By.id("BillingNewAddress_Address1");
    By billingZipCode = By.id("BillingNewAddress_ZipPostalCode");
    By billingPhoneNumber = By.id("BillingNewAddress_PhoneNumber");
    By billingContinueBtn = By.cssSelector("button.new-address-next-step-button");

    // Shipping and Payment Locators
    By shippingMethodContinueBtn = By.xpath("//button[@class='button-1 shipping-method-next-step-button']");
    By paymentMethodContinueBtn = By.xpath("//button[@class='button-1 payment-method-next-step-button']");
    By paymentInfoContinueBtn = By.xpath("//button[@class='button-1 payment-info-next-step-button']");
    By confirmOrderBtn = By.xpath("//button[@class='button-1 confirm-order-next-step-button']");

    @BeforeMethod
    public void beforeMethod() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCartCheckoutUpToConfirmOrder() throws Exception {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("TC_E2E_005 - Login > Add to Cart > Checkout (No Payment) > Logout");

        // Step 1: Login
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        login.click();
        test.pass("Clicked on login");

        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(input_Email));
        WebElement password = driver.findElement(input_Password);
        email.sendKeys("madanaaparna@gmail.com");
        password.sendKeys("Aparna");
        test.pass("Entered email and password");

        driver.findElement(btn_Login).click();

        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
        if (logout.isDisplayed()) {
            test.pass("Login successful");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginFailed");
            test.fail("Login failed").addScreenCaptureFromPath(screenPath);
            return;
        }

        // Step 2: Navigate to Books category and add to cart
        driver.findElement(By.linkText("Books")).click();
        test.pass("Navigated to Books");

        WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(@class,'add-to-cart-button')])[1]")));
        addToCart.click();
        test.pass("Clicked Add to Cart");

        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationBar));
        if (notification.getText().toLowerCase().contains("added")) {
            test.pass("Product added to cart");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Cart_Add_Failed");
            test.fail("Product not added to cart").addScreenCaptureFromPath(screenPath);
            return;
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(notificationBar));

        // Step 3: Navigate to Cart and Checkout
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartIcon));
        cart.click();
        test.pass("Navigated to Cart");

        WebElement agreeCheckboxElement = wait.until(ExpectedConditions.elementToBeClickable(agreeCheckbox));
        agreeCheckboxElement.click();

        WebElement checkoutBtnElement = wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn));
        checkoutBtnElement.click();
        test.pass("Clicked Checkout");

        // Step 4: Fill Billing Address
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));

            driver.findElement(billingFirstName).sendKeys("John");
            driver.findElement(billingLastName).sendKeys("Doe");
            driver.findElement(billingEmail).sendKeys("anilkumarraju32.com");

            Select countryDropdown = new Select(driver.findElement(billingCountry));
            // 🐛 FIX: Corrected text from "United States" to "United States of America"
            countryDropdown.selectByVisibleText("United States of America");

            wait.until(ExpectedConditions.presenceOfElementLocated(billingState));
            Select stateDropdown = new Select(driver.findElement(billingState));
            stateDropdown.selectByVisibleText("California");

            driver.findElement(billingCity).sendKeys("Los Angeles");
            driver.findElement(billingAddress1).sendKeys("123 Main St");
            driver.findElement(billingZipCode).sendKeys("90210");
            driver.findElement(billingPhoneNumber).sendKeys("1234567890");

            test.pass("Filled billing address details");

            WebElement billingContinue = wait.until(ExpectedConditions.elementToBeClickable(billingContinueBtn));
            billingContinue.click();
            test.pass("Clicked Billing Continue button");

        } catch (UnhandledAlertException e) {
            driver.switchTo().alert().dismiss();
            String screenPath = ScreenshotUtiles.Capture(driver, "BillingAddressAlert");
            test.fail("Alert was present on billing page: " + e.getAlertText()).addScreenCaptureFromPath(screenPath);
            return;
        }

        // Step 5: Shipping Method
        WebElement shippingMethodContinue = wait.until(ExpectedConditions.elementToBeClickable(shippingMethodContinueBtn));
        shippingMethodContinue.click();
        test.pass("Shipping method selected and continued");

        // Step 6: Payment Method
        WebElement paymentMethodContinue = wait.until(ExpectedConditions.elementToBeClickable(paymentMethodContinueBtn));
        paymentMethodContinue.click();
        test.pass("Payment method selected and continued");

        // Step 7: Payment Info
        WebElement paymentInfoContinue = wait.until(ExpectedConditions.elementToBeClickable(paymentInfoContinueBtn));
        paymentInfoContinue.click();
        test.pass("Payment info continued");

        // Step 8: Confirm Order
        WebElement confirmOrderBtnElement = wait.until(ExpectedConditions.elementToBeClickable(confirmOrderBtn));
        if (confirmOrderBtnElement.isDisplayed()) {
            test.pass("Reached Confirm Order page successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "ConfirmOrder_NotReached");
            test.fail("Failed to reach Confirm Order page").addScreenCaptureFromPath(screenPath);
        }

        // Step 9: Logout
        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        logoutBtn.click();
        test.pass("Logout clicked");

        WebElement loginAgain = wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        if (loginAgain.isDisplayed()) {
            test.pass("User logged out successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LogoutFailed");
            test.fail("Logout failed").addScreenCaptureFromPath(screenPath);
        }
  }
}