package com.capstone.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
 
import com.aventstack.extentreports.ExtentReports;
import com.capstone.utilities.ExtentManager;
 
import io.github.bonigarcia.wdm.WebDriverManager;
 
public class BaseTest {
 
    protected WebDriver driver;   // 👈 protected so child classes can access
    protected ExtentReports extent;
 
    @BeforeSuite
    public void setupreport() {
        extent = ExtentManager.getinstance();
    }
 
    @BeforeMethod
    public void setup() {
        System.out.println("Before method");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();   // 👈 maximize here
        driver.get("https://demo.nopcommerce.com");
    }
 
    @AfterMethod
    public void teardown() {
        System.out.println("After method");
        if (driver != null) {
            driver.quit();
        }
    }
 
    @AfterSuite
    public void flushreport() {
        extent.flush();
    }
}