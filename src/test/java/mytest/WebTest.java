package mytest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebTest {

    public static void main(String[] args) {
        // 使用 WebDriverManager 来设置 ChromeDriver 的路径
        WebDriverManager.chromedriver().setup();

        // 创建 WebDriver 实例
        WebDriver driver = new ChromeDriver();

        // ... 你的测试代码 ...

        driver.quit();
    }
    @Test
    public void hello() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Welcome\\Desktop\\SEM 2\\QA\\chromedriver.exe");
        WebDriver driver  = new ChromeDriver();
        driver.get("http://mail.google.com/mail/");
        driver.findElement(By.xpath("//input[@id='identifierId']")).sendKeys("username");

        driver.findElements(By.className("RveJvd")).get(0).click();
        Thread.sleep(2000);

        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElements(By.className("RveJvd")).get(1).click();
        //driver.quit();
    }

}
