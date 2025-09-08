package pack1;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Lab6 {
public static void main(String[] args) throws InterruptedException {
        // Setup Chrome
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1: Open Login Page
        driver.get("https://tutorialsninja.com/demo/index.php?route=account/login");

        // Step 2: Login
        driver.findElement(By.id("input-email")).sendKeys("madanaaparna1@gmail.com"); // replace with real email
        driver.findElement(By.id("input-password")).sendKeys("1234567890");   // replace with real password
        driver.findElement(By.xpath("//input[@value='Login']")).click();

        // Step 3: Go to 'Components' -> 'Monitors'
        driver.findElement(By.linkText("Components")).click();
        driver.findElement(By.linkText("Monitors (2)")).click();

        // Step 4: Select '25' from 'Show' dropdown
        WebElement showDropdown = driver.findElement(By.id("input-limit"));
        showDropdown.findElement(By.xpath("//option[text()='25']")).click();

        // Step 5: Open first monitor product
        driver.findElement(By.linkText("Apple Cinema 30\"")).click();

        // Step 6: Click 'Specification' tab
        driver.findElement(By.linkText("Specification")).click();

        // Step 7: Verify Specification tab
        if (driver.findElement(By.id("tab-specification")).isDisplayed()) {
            System.out.println("✅ Specification details displayed");
        } else {
            System.out.println("❌ Specification details not displayed");
        }

        // Step 8: Add to Wish List
        driver.findElement(By.xpath("//button[@data-original-title='Add to Wish List']")).click();

        // Step 9: Verify Wish List success message
        WebElement wishMsg = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("div.alert.alert-success")));
        String wishText = wishMsg.getText().trim();
        if (wishText.startsWith("×")) {
            wishText = wishText.substring(1).trim();
        }
        System.out.println("Wish List Message: " + wishText);

        if (wishText.contains("Apple Cinema 30\"")) {
            System.out.println("✅ Wish list success message displayed");
        } else {
            System.out.println("❌ Wish list success message not displayed");
        }

        // Step 10: Search for 'Mobile'
        driver.findElement(By.name("search")).sendKeys("Mobile");
        driver.findElement(By.cssSelector("button.btn.btn-default.btn-lg")).click();

        // Step 11: Enable 'Search in product descriptions' & search again
        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("button-search")).click();

        // Step 12: Open 'HTC Touch HD'
        driver.findElement(By.linkText("HTC Touch HD")).click();

        // Step 13: Change Qty to 3
        WebElement qtyBox = driver.findElement(By.id("input-quantity"));
        qtyBox.clear();
        qtyBox.sendKeys("3");

        // Step 14: Add to Cart
        driver.findElement(By.id("button-cart")).click();

        // Step 15: Verify Cart success message
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement cartMsg = wait1.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert.alert-success"))
        );
        String cartText = cartMsg.getText().trim();
        if (cartText.startsWith("×")) {
            cartText = cartText.substring(1).trim();
        }
        System.out.println("Cart Message: " + cartText);

        if (cartText.contains("HTC Touch HD")) {
            System.out.println("✅ Cart success message displayed");
        } else {
            System.out.println("❌ Cart success message not displayed");
        }

        // step 16:
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(
                cartMsg.findElement(By.linkText("shopping cart"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);


        // Step 17: Verify product in cart
        if (driver.getPageSource().contains("HTC Touch HD")) {
            System.out.println("✅ HTC Touch HD present in shopping cart");
        } else {
            System.out.println("❌ HTC Touch HD not found in shopping cart");
        }
        // step 18
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Checkout")));
        checkoutBtn.click();

        // Step 19: Logout
        WebDriverWait wait11 = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click My Account to expand menu
        WebElement myAccountMenu = driver.findElement(By.xpath("//a[@title='My Account']"));
        myAccountMenu.click();

        // Wait for Logout link to be clickable
        WebElement logoutLink = wait11.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        logoutLink.click();
        // Step 20: Verify Logout
        if (driver.getPageSource().contains("Account Logout")) {
            System.out.println("✅ Account Logout page displayed");
        } else {
            System.out.println("❌ Account Logout page not displayed");
        }

        // Step 21: Continue
        driver.findElement(By.linkText("Continue")).click();

        // Final Step: Close browser
        driver.quit();

        System.out.println("🎉 Test execution finished!");
    }
}
