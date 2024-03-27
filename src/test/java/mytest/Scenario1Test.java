package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.print.PrintOptions;
//import org.openqa.selenium.print.PrintOutput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scenario1Test {

    private WebDriver driver;
    private WebDriverWait wait;
    private String username;
    private String password;
    private final String scenarioFolder = "screenshots" + File.separator + "DownloadLatestTranscript" + File.separator;

    public Scenario1Test() throws IOException {
    }

    @BeforeClass
    public void setUp() throws IOException {
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
        username = row.getCell(0).getStringCellValue();
        password = row.getCell(1).getStringCellValue();
        file.close();
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    @Test
    public void downloadLatestTranscript() throws InterruptedException, IOException {
        // Go to the login page and take a screenshot
        driver.get("https://me.northeastern.edu");

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
        Thread.sleep(1000);
        signInButton1.click();

        // Find the password input element by its CSS selector and send the username
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("i0118")));
        Thread.sleep(1000);
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
        
        WebElement resourceTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("a[data-testid='link-resources']")));
        Thread.sleep(2000); // Wait for 2 seconds

        takeScreenshot("03_AfterLogin");
        resourceTab.click();
        WebElement academicResources = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > span:nth-child(2)")));
        Thread.sleep(2000); // Wait for 2 seconds
        takeScreenshot("04_AfterClickingResources");
        academicResources.click();

        WebElement transcript = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("li.ResourceLink_resourceLink__ddWwB:nth-child(20) > div:nth-child(2) > a:nth-child(1)")));
        Thread.sleep(2000); // Wait for 2 seconds
        takeScreenshot("05_AfterClickingAcademicAndClass");
        transcript.click();

        takeScreenshot("06_LoginMyNEU");
        // Read username and password from Excel file
        FileInputStream file = new FileInputStream(new File("./src/test/resources/username.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        username = row.getCell(0).getStringCellValue();
        password = row.getCell(1).getStringCellValue();
        System.out.println(username);
        System.out.println(password);
        file.close();
        Thread.sleep(5000); // Wait for 2 seconds

        // WORKAROUND TO NAVIGATE THE DRIVER TO THE REQUIRED TAB
        // Get all window handles
        Set<String> handles = driver.getWindowHandles();
        // Loop through each handle
        for (String handle : handles) {
            // Switch to the window
            driver.switchTo().window(handle);

            // Example: Switch to the tab with a specific title
            if (driver.getTitle().equals("Log in")) {
                break; // Exit the loop if the tab is found
            }
        }
        List<WebElement> frames = driver.findElements(By.tagName("input"));
        System.out.println(frames);
        frames.get(0).click();
        frames.get(0).sendKeys(username);
        frames.get(1).sendKeys(password);
        takeScreenshot("07_BeforeLoginMyNEU");
        WebElement loginBtn = driver.findElement(
                By.cssSelector("button.form-element"));
        Thread.sleep(2000); // Wait for 2 seconds
        takeScreenshot("08_AfterEnteringCredentials");
        loginBtn.click();

        WebElement transcriptLevel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("levl_id")));
        Thread.sleep(2000); // Wait for 2 seconds
        takeScreenshot("09_AfterClickingTranscript");
        Select dropdown = new Select(transcriptLevel);
        dropdown.selectByValue("GR");
        Thread.sleep(2000); // Wait for 2 seconds
        takeScreenshot("10_BeforeSubmit");
        WebElement submitBtn = driver.findElement(
                By.cssSelector(".pagebodydiv > form:nth-child(2) > input:nth-child(2)"));
        submitBtn.click();

        Thread.sleep(5000);

        // Use JavaScript to trigger the print dialog
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.print();");

        // Wait for the print dialog to appear (you may need to adjust the wait time)
        try {
            Thread.sleep(5000); // Wait for 5 seconds (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate a click on the "Save" button in the print dialog
        js.executeScript("window.onafterprint = function() { document.querySelector('#print-preview-toolbar').shadowRoot.querySelector('#sidebar').querySelector('#sidebar-toolbar').querySelector('button[data-l10n-id=print_save_as_pdf_label]').click(); };");

        // Wait for the file to be saved
        try {
            Thread.sleep(5000); // Wait for 5 seconds (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

