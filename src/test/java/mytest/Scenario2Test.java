package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private String username;
    private String password;
    private final String screenshotFolder = "screenshots" + File.separator + "Scenario2" + File.separator;

    @BeforeClass
    public void setUp() throws IOException {
        // Set up ChromeDriver path
//        WebDriverManager.chromedriver().setup();
        //WebDriverManager.chromedriver().browserVersion("122.0.6261.94").setup();
        driver = new FirefoxDriver();
        WebDriverManager.firefoxdriver().setup();
        //driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        // Create a folder to store screenshots
        String screenshotsFolder = "./src/test/resources/screenshots/Scenario2/";
        new File(screenshotsFolder).mkdirs();

        // Read username and password from Excel file
        FileInputStream file = new FileInputStream(new File("./src/test/resources/username.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        username = row.getCell(0).getStringCellValue();
        password = row.getCell(1).getStringCellValue();
        file.close();
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

    }

    @Test
    public void addTodoTasks() throws IOException, InterruptedException {
        driver.get("https://northeastern.instructure.com/");
        Thread.sleep(2000); // Wait for 2 seconds
        takeScreenshot("01_BeforeLogin");

        // Find the username input element by its CSS selector and send the username
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("i0116")));
        usernameInput.click();
        usernameInput.sendKeys(username);

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("idSIButton9")));
        Thread.sleep(2000);
        signInButton1.click();

        // Find the password input element by its CSS selector and send the username
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("i0118")));
        Thread.sleep(2000);
        passwordInput.click();
        passwordInput.sendKeys(password);
        takeScreenshot("02_AfterEnteringCredentials");

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton2 = driver.findElement(By.id("idSIButton9"));
        signInButton2.click();

        WebElement signInButton3 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("idSIButton9")));
        signInButton3.click();
        WebElement signInButton4 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("idSIButton9")));
        signInButton4.click();

        WebElement calendar = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("global_nav_calendar_link")));
        takeScreenshot("00_AfterLoadingCanvas");
        calendar.click();

        // Read event details from Excel
        List<String[]> eventDetailsList = readExcel();

        // Add events
        for (String[] eventDetails : eventDetailsList) {
            addEvent(eventDetails);
            Thread.sleep(2000);
        }
    }
private void addEvent(String[] eventDetails) throws IOException, InterruptedException {
    WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("create_new_event_link")));
    takeScreenshot("01_AfterClickingCalendar");
    addButton.click();

    WebElement titleElement = wait.until(ExpectedConditions.elementToBeClickable(
            By.id("TextInput_0")));
    takeScreenshot("02_AfterClickingAdd");
    titleElement.click();
    titleElement.sendKeys(eventDetails[0]);

    WebElement dateElement = driver.findElement(
            By.id("Selectable_0"));
    dateElement.click();
    dateElement.sendKeys(eventDetails[1]);
    dateElement.click();

    WebElement startTimeElement = driver.findElement(By.id("Select_0"));
    startTimeElement.click();
    startTimeElement.sendKeys(eventDetails[2]);
    startTimeElement.click();

    WebElement endTimeElement = driver.findElement(By.id("Select_1"));
    endTimeElement.click();
    endTimeElement.sendKeys(eventDetails[3]);
    endTimeElement.click();

    WebElement locationElement = driver.findElement(By.id("TextInput_5"));
    locationElement.click();
    locationElement.sendKeys(eventDetails[4]);

    takeScreenshot("03_BeforeSubmit");
    Thread.sleep(2000);
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
            DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale

            String title = formatter.formatCellValue(row.getCell(0));
            String date = formatter.formatCellValue(row.getCell(1));
            String startTime = formatter.formatCellValue(row.getCell(2));
            String endTime = formatter.formatCellValue(row.getCell(3));
            String location = formatter.formatCellValue(row.getCell(4));

            String[] eventDetails = {title, date, startTime, endTime, location};
            eventDetailsList.add(eventDetails);
        }
        workbook.close();
        file.close();
        return eventDetailsList;
        }
}
