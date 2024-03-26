package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Scenario2Test {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String screenshotFolder = "screenshots" + File.separator + "Scenario2" + File.separator;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        new File(screenshotFolder).mkdirs();
    }

    @Test
    public void addTodoTasks() throws IOException, InterruptedException {
        driver.get("https://canvas.instructure.com");
        // Assume there are methods for login and navigation
        // loginToCanvas();
        // navigateToCalendar();

        // Click on the "+" button to add a new event
        WebElement addButton = driver.findElement(By.cssSelector("selector-for-add-button")); // Replace with actual selector
        addButton.click();
        takeScreenshot("01_AfterClickingAdd");

        // Read event details from Excel
        FileInputStream file = new FileInputStream(new File("path/to/excel.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < 2; i++) {
            Row row = sheet.getRow(i);
            String title = row.getCell(0).getStringCellValue();
            // Continue for Date, Time, Calendar, Details...

            // Fill in the event details in the form
            WebElement titleElement = driver.findElement(By.cssSelector("selector-for-title")); // Replace with actual selector
            titleElement.sendKeys(title);
            // Continue for other details...
            takeScreenshot("02_BeforeEvent" + (i + 1) + "Submit");

            // Submit the event
            WebElement submitButton = driver.findElement(By.cssSelector("selector-for-submit-button")); // Replace with actual selector
            submitButton.click();
            takeScreenshot("03_AfterEvent" + (i + 1) + "Submit");

            // Verify the event is added if needed and perform other steps to ready for the next iteration if necessary
        }
        file.close();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    private void takeScreenshot(String stepName) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File destFile = new File(screenshotFolder + stepName + "_" + timeStamp + ".png");
        FileUtils.copyFile(scrFile, destFile);
    }
}
