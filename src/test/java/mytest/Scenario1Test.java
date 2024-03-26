package mytest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.print.PrintOutput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.io.IOException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        // Set up ChromeDriver path
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--kiosk-printing");
        // Initialize WebDriver
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver = new ChromeDriver(options);

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
    }

    @Test
    public void downloadLatestTranscript() throws InterruptedException, IOException {
        // Go to the login page and take a screenshot
        driver.get("https://me.northeastern.edu");
        takeScreenshot("01_BeforeLogin");

        // Find the username input element by its CSS selector and send the username
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("i0116")));
        usernameInput.click();
        usernameInput.sendKeys(username);

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("idSIButton9")));
        signInButton1.click();

        // Find the password input element by its CSS selector and send the username
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("i0118")));
        passwordInput.click();
        passwordInput.sendKeys(password);

        // Find the sign-in button by its CSS selector and click it
        WebElement signInButton2 = driver.findElement(By.id("idSIButton9"));
        signInButton2.click();
        
        WebElement resourceTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("a[data-testid='link-resources']")));

        takeScreenshot("02_AfterLogin");
        resourceTab.click();
        WebElement academicResources = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#resource-tab-Academic_Resources > span:nth-child(2)")));
        takeScreenshot("04_AfterClickingResources");
        academicResources.click();

        WebElement transcript = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("li.ResourceLink_resourceLink__ddWwB:nth-child(20) > div:nth-child(2) > a:nth-child(1)")));
        takeScreenshot("05_AfterClickingAcademicAndClass");
        transcript.click();
        WebElement transcriptLevel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("levl_id")));
        takeScreenshot("06_AfterClickingTranscript");
        Select dropdown = new Select(transcriptLevel);
        dropdown.selectByValue("GR");
        takeScreenshot("07_BeforeSubmit");
        WebElement submitBtn = driver.findElement(
                By.cssSelector(".pagebodydiv > form:nth-child(2) > input:nth-child(2)"));
        submitBtn.click();

        String currentUrl = driver.getCurrentUrl();
        // Create a PrintOptions object
        PrintOptions printOptions = new PrintOptions();
        printOptions.setPageRanges("1-2");

        // Execute print command and get the print output
        PrintOutput printOutput = driver.print(printOptions);

        // Get the content of the PDF as a base64 string
        String base64EncodedPDF = printOutput.getContent();

        // Convert the base64 string to bytes and write to a file
        byte[] decodedPDF = java.util.Base64.getDecoder().decode(base64EncodedPDF);
        try (FileOutputStream fos = new FileOutputStream("transcript.pdf")) {
            fos.write(decodedPDF);
        } catch (IOException e) {
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

