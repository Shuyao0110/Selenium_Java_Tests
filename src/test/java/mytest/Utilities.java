package mytest;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    final String DOWNLOAD_DIR = "./src/test/resources/downloads/";
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    public FirefoxOptions setDownloadOptions(){
        String downloadsFolder = "./src/test/resources/downloads/";
        new File(downloadsFolder).mkdirs();
        // Create a Firefox profile
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.dir", downloadsFolder);
        profile.setPreference("browser.download.folderList", 2); // 0: Desktop, 1: Downloads, 2: Custom

        // Create FirefoxOptions and set the profile
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);

        return options;
    }

    public boolean checkForDownloadStatus(WebDriver driver){
        try {
            // Wait for the download to complete
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // 60 seconds timeout
            wait.until((WebDriver d) -> {
                File dir = new File(DOWNLOAD_DIR);
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().endsWith(".crdownload")) {
                            return false; // File is still downloading
                        }
                    }
                    return true; // All files have finished downloading
                }
                return false; // No files found in the directory
            });
            return true; // Download completed successfully
        } catch (TimeoutException e) {
            return false; // Timeout occurred, download did not complete
        }
    }

    public void clearCookies(String website, WebDriver driver, boolean clearAll){
        try {
            if(clearAll){
                driver.manage().deleteAllCookies();
            } else {
                // Get all cookies
                Set<Cookie> cookies = driver.manage().getCookies();

                // Iterate over the cookies
                for (Cookie cookie : cookies) {
                    // Check if the cookie's domain matches the target domain
                    if (cookie.getDomain().contains(website)) {
                        // Delete the cookie
                        driver.manage().deleteCookie(cookie);
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public String extractUsername(String email) {
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

    public void takeScreenshot(WebDriver driver, String stepName, int scenarioNo) throws Exception {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = formatter.format(new Date());
        File destFile = new File("screenshots/scenario" + scenarioNo + stepName + "_" + timestamp + ".png");
        FileUtils.copyFile(scrFile, destFile);
    }

    public void navigateFocusToNewTab(String newTab, WebDriver driver){
        // Get all window handles
        Set<String> handles = driver.getWindowHandles();
        // Loop through each handle
        for (String handle : handles) {
            // Switch to the window
            driver.switchTo().window(handle);

            // Example: Switch to the tab with a specific title
            if (driver.getTitle().startsWith(newTab)) {
                break; // Exit the loop if the tab is found
            }
        }
    }
}
