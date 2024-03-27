package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class Scenario5Test {

    private WebDriver driver;
    private WebDriverWait wait;
    private String username;
    private String password;
    private Utilities utils;
    private final String scenarioFolder = "screenshots" + File.separator + "DownloadLatestTranscript" + File.separator;

    public Scenario5Test() throws IOException {
    }

    @BeforeClass
    public void setUp() throws IOException {
        utils = new Utilities();
        // Set up GeckoDriver path
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        // Set Firefox profile to save as PDF
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("print.print_to_file", true);
        profile.setPreference("print.print_to_filename", "screenshots/transcripts/pdf.pdf");
        options.setProfile(profile);
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Create a folder to store screenshots
        String screenshotsFolder = "./src/test/resources/screenshots/Scenario1/";
        new File(screenshotsFolder).mkdirs();

        // Read username and password from Excel file
        FileInputStream file = new FileInputStream(new File("./src/test/resources/username.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        username = "ghatage.s@northeastern.edu"; // row.getCell(0).getStringCellValue();
        password = "Dhiru@123456789"; // row.getCell(1).getStringCellValue();
        file.close();
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    @Test
    public void downloadLatestTranscript() throws Exception {
        // Go to the login page and take a screenshot
        driver.get("https://me.northeastern.edu");

        Thread.sleep(2000); // Wait for 2 seconds
        utils.takeScreenshot(driver,"01_BeforeLogin", 5);

        // Find the username input element by its CSS selector and send the username
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("i0116")));
        usernameInput.click();
        usernameInput.sendKeys(username);

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("idSIButton9")));
        Thread.sleep(1000);
        signInButton1.click();

        // Find the password input element by its CSS selector and send the username
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("i0118")));
        Thread.sleep(1000);
        passwordInput.click();
        passwordInput.sendKeys(password);
        utils.takeScreenshot(driver,"02_AfterEnteringCredentials", 5);

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton2 = driver.findElement(By.id("idSIButton9"));
        signInButton2.click();

        WebElement signInButton3 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("idSIButton9")));
        signInButton3.click();
        WebElement signInButton4 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("idSIButton9")));
        signInButton4.click();

        WebElement resourceTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("a[data-testid='link-resources']")));
        Thread.sleep(2000); // Wait for 2 seconds

        utils.takeScreenshot(driver,"03_AfterLogin", 5);
        resourceTab.click();
        WebElement academicResources = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > span:nth-child(2)")));
        Thread.sleep(2000); // Wait for 2 seconds

        utils.takeScreenshot(driver,"04_AfterClickingResources", 5);
        academicResources.click();

        WebElement academicCalendarLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("li.ResourceLink_resourceLink__ddWwB:nth-child(1) > div:nth-child(2) > a:nth-child(1)")));
        Thread.sleep(2000); // Wait for 2 seconds

        utils.takeScreenshot(driver,"05_AfterClickingAcademicAndClass", 5);
        academicCalendarLink.click();

        Thread.sleep(5000);

        utils.navigateFocusToNewTab("Calendar", driver);

        WebElement academicCalendarHref = driver.findElement(By.xpath("//*[@id=\"tax-academic-calendars\"]/div/a[1]"));
        academicCalendarHref.click();

        Thread.sleep(5000);
        //WebElement calendarDegrees = driver.findElement(By.xpath("//*[@id=\"twCalendarListNameCellFirst\"]/table/tbody/tr/td[2]/table/tbody/tr/td/a"));

        // Assuming 'driver' is your WebDriver instance
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

//        List<WebElement> degrees = driver.findElements(By.className("twCalendarListCheckbox"));
//        if(!degrees.isEmpty()){
//            if (degrees.get(1).isSelected()) {
//                wait.until(ExpectedConditions.elementToBeClickable(degrees.get(1)));
//                degrees.get(1).click();
//            }
//        }
        utils.navigateFocusToNewTab("Academic Calendar", driver);
        //WebElement checkbox = driver.findElement(By.id("mixItem0"));

        Thread.sleep(2000);

        // PENDING  -> Button assertion
//        WebElement transcriptLevel = driver.findElement(By.xpath("//*[@id=\"ctl04_ctl53_ctl00_buttonAtmc\"]"));
//                //wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl04_ctl152_ctl00_buttonAtmc")));
//        if(transcriptLevel == null){
//            throw new Exception("Add to my calendar not displayed");
//        }

        Thread.sleep(3000);
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}


