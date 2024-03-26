package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.time.Duration;

public class Scenario1Test {

    private WebDriver driver;
    private final String scenarioFolder = "screenshots" + File.separator + "DownloadLatestTranscript" + File.separator;

    @BeforeClass
    public void setUp() {
        // Set up ChromeDriver path
        //System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriverManager.chromedriver().setup();
        // Initialize WebDriver
        driver = new ChromeDriver();
        // Create scenario folder
        new File(scenarioFolder).mkdirs();
    }
    String username = "he.shuyao@northeastern.edu";
    @Test
    public void downloadLatestTranscript() throws InterruptedException, IOException {
        // Go to the login page and take a screenshot
        driver.get("https://me.northeastern.edu");
        takeScreenshot("01_BeforeLogin");

        // Find the username input element by its CSS selector and send the username
        WebElement usernameInput = driver.findElement(By.cssSelector("#i0116"));
        usernameInput.sendKeys(username);

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton1 = driver.findElement(By.cssSelector("#idSIButton9"));
        signInButton1.click();

        // Find the password input element by its CSS selector and send the username
        WebElement passwordInput = driver.findElement(By.cssSelector("#i0116"));
        usernameInput.sendKeys(password);

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton2 = driver.findElement(By.cssSelector("#idSIButton9"));
        signInButton2.click();

        takeScreenshot("02_AfterLogin");

        WebDriver driver = new ChromeDriver();
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[data-testid='link-resources']")));

        takeScreenshot("03_BeforeClickingResources");

        // Click the Resources tab and take a screenshot after
        WebElement resourcesTab = driver.findElement(By.xpath("//a[@data-testid='link-resources']"));
        resourcesTab.click();
        takeScreenshot("04_AfterClickingResources");

        // Continue with other steps and take screenshots accordingly
        // ...
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String stepName) throws IOException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File destFile = new File(scenarioFolder + stepName + "_" + timeStamp + ".png");
        FileUtils.copyFile(scrFile, destFile);
    }
}

