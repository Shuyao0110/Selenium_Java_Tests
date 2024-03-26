package mytest;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Scenario3Test {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String scenarioFolder = "screenshots" + File.separator + "Scenario3" + File.separator;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofMinutes(1));
        String screenshotsFolder = "./src/test/resources/screenshots/Scenario2/";
        new File(screenshotsFolder).mkdirs();
    }

    @Test
    public void downloadClassroomGuide() throws Exception {
        driver.get("https://service.northeastern.edu/tech?id=classrooms");
        takeScreenshot("01_AfterLoadingClassrooms");

        WebElement classroomToSelect = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"x77ea03d9972dd1d8beddb4221153afa6\"]/div/div[2]/span/div/div/div[5]/div/div/div/a"))); // Replace with actual selector
        classroomToSelect.click();
        takeScreenshot("02_AfterSelectingClassroom");

        WebElement nuflexLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("NUFlex Auto and Manual Classroom")));
        nuflexLink.click();
        takeScreenshot("03_AfterSelectingNUFlexLink");

        WebElement downloadLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("#downloads"))); // Replace with actual selector for the PDF download link
        downloadLink.click();
        // Assuming the browser is configured to automatically download the file to a specified location.
        takeScreenshot("04_AfterClickingDownload");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    private void takeScreenshot(String stepName) throws Exception {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = formatter.format(new Date());
        FileUtils.copyFile(scrFile, new File(scenarioFolder + stepName + "_" + timestamp + ".png"));
    }
}
