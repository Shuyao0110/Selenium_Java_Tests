package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Scenario2Test {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String screenshotFolder = "screenshots" + File.separator + "Scenario2" + File.separator;

    @BeforeClass
    public void setUp() {
        // Set up ChromeDriver path
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        // Create a folder to store screenshots
        String screenshotsFolder = "./src/test/resources/screenshots/Scenario2/";
        new File(screenshotsFolder).mkdirs();

    }

    @Test
    public void addTodoTasks() throws IOException, InterruptedException {
        driver.get("https://northeastern.instructure.com/");
        WebElement calendar = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("global_nav_calendar_link")));
        takeScreenshot("00_AfterLoadingCanvas");
        calendar.click();

        // Read event details from Excel
        List<String[]> eventDetailsList = readExcel();

        // Add events
        for (String[] eventDetails : eventDetailsList) {
            addEvent(eventDetails);
        }
    }
private void addEvent(String[] eventDetails) throws IOException {
    WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("create_new_event_link")));
    takeScreenshot("01_AfterClickingCalendar");
    addButton.click();

    WebElement titleElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("textInput_7")));
    takeScreenshot("02_AfterClickingAdd");
    titleElement.click();
    titleElement.sendKeys(eventDetails[0]);

    WebElement dateElement = driver.findElement(By.id("selectable_0"));
    dateElement.click();
    dateElement.sendKeys(eventDetails[1]);

    WebElement startTimeElement = driver.findElement(By.id("Select_0"));
    startTimeElement.click();
    startTimeElement.sendKeys(eventDetails[2]);

    WebElement endTimeElement = driver.findElement(By.id("Select_1"));
    endTimeElement.click();
    endTimeElement.sendKeys(eventDetails[3]);

    WebElement locationElement = driver.findElement(By.id("TextInput_5"));
    locationElement.click();
    locationElement.sendKeys(eventDetails[4]);

    takeScreenshot("03_BeforeSubmit");

    WebElement submitButton = driver.findElement(By.cssSelector("#edit_calendar_event_form_holder > form > fieldset > span > span > span > span > span > span > span.css-1sbhkbz-gridCol > span > span.css-ude3jz-view-flexItem > span > button > span"));
    submitButton.click();
    takeScreenshot("04_AfterSubmit");
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

    public List<String[]> readExcel() throws IOException {
        FileInputStream file = new FileInputStream(new File("./src/test/resources/events.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<String[]> eventDetailsList = new ArrayList<>();

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String title = row.getCell(0).getStringCellValue();
            String date = row.getCell(1).getStringCellValue();
            String startTime = row.getCell(2).getStringCellValue();
            String endTime = row.getCell(3).getStringCellValue();
            String location = row.getCell(4).getStringCellValue();
            String[] eventDetails = {title, date, startTime, endTime, location};
            eventDetailsList.add(eventDetails);
        }
        workbook.close();
        file.close();
        return eventDetailsList;
        }
}
