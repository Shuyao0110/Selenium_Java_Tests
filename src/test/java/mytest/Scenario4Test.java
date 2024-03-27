package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class Scenario4Test {
    private WebDriver driver;
    private WebDriverWait wait;

    private Utilities utils;
    @BeforeClass
    public void setUp() {
        utils = new Utilities();
        // Initialize FirefoxDriver with the modified options
        driver = new FirefoxDriver(utils.setDownloadOptions());

        WebDriverManager.firefoxdriver().setup();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String screenshotsFolder = "./src/test/resources/screenshots/Scenario3/";
        new File(screenshotsFolder).mkdirs();
    }

    @Test
    public void downloadDataset() throws Exception {
        driver.manage().window().maximize();

        //Clear the cookies to always have the same work flow
        utils.clearCookies("https://onesearch.library.northeastern.edu/", driver, false);

        driver.get("https://onesearch.library.northeastern.edu/discovery/search?vid=01NEU_INST:NU&lang=en");
        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[./span[text()='digital repository service']]")));

        // Take screenshot of library page before clicking repository
         utils.takeScreenshot(driver, "01_AfterLoadingClassrooms", 4);

        linkElement.click();
        Thread.sleep(5000);

        // WORKAROUND TO NAVIGATE THE DRIVER TO THE REQUIRED TAB
        utils.navigateFocusToNewTab("DRS", driver);

        //Take screenshot of repository page
        List<WebElement> linkElements1 = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("btn-block"), 0));
        if (!linkElements1.isEmpty()) {
            linkElements1.get(4).click();
        }

        //Take screenshot of all datasets
        List<WebElement> linkElements = driver.findElements(By.xpath("//a[starts-with(@href, '/downloads/neu:')]"));
        if (!linkElements.isEmpty()) {
            linkElements.get(0).click();
        }

        // Delay to complete download
        Thread.sleep(10000);
        utils.checkForDownloadStatus(driver);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
