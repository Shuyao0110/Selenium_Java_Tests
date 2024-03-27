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
        //options.addArguments("--headless");
// Initialize WebDriver
        //driver = new FirefoxDriver(options);
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
        Thread.sleep(2000); // Wait for 2 seconds
        List<WebElement> frames = driver.findElements(By.tagName("input"));
        frames.get(0).click();
        frames.get(0).sendKeys(username);
        frames.get(1).sendKeys(password);
//        WebElement userInput = wait.until(ExpectedConditions.elementToBeClickable(
//                By.id("username")));
//        Thread.sleep(2000); // Wait for 2 seconds
//        userInput.click();
//        userInput.sendKeys(extractUsername(username));
//        WebElement passInput = wait.until(ExpectedConditions.elementToBeClickable(
//                By.id("password")));
//        passInput.click();
//        passInput.sendKeys(password);
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


        String currentUrl = driver.getCurrentUrl();
        // Create a PrintOptions object
        PrintOptions printOptions = new PrintOptions();
        printOptions.setPageRanges("1-2");

        String command = "Page.printToPDF";
        Map<String, Object> params = new HashMap<>();
        params.put("landscape", false);
        Map<String, Object> output = ((ChromiumDriver)driver).executeCdpCommand(command, params);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("export.pdf");
            byte[] byteArray = Base64.getDecoder().decode((String)output.get("data"));
            fileOutputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Execute print command and get the print output
//        PrintOutput printOutput = driver.print(printOptions);
//
//        // Get the content of the PDF as a base64 string
//        String base64EncodedPDF = printOutput.getContent();
//
//        // Convert the base64 string to bytes and write to a file
//        byte[] decodedPDF = java.util.Base64.getDecoder().decode(base64EncodedPDF);
//        try (FileOutputStream fos = new FileOutputStream("transcript.pdf")) {
//            fos.write(decodedPDF);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
    public static String extractUsername(String email) {
        // Regular expression to match the part before the '@' symbol
        String regex = "^(.*?)@";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "No match found";
        }
    }
    private void takeScreenshot(String stepName) throws IOException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File destFile = new File(scenarioFolder + stepName + "_" + timeStamp + ".png");
        FileUtils.copyFile(scrFile, destFile);
    }
}

