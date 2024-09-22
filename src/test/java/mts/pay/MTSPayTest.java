package mts.pay;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MTSPayTest {
    ChromeOptions options = new ChromeOptions();
    static WebDriver driver;
    WebElement payForm;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @SneakyThrows
    @BeforeEach
    public void test() {
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("https://www.mts.by/");
        Thread.sleep(5000);
        if (driver.findElement(By.id("cookie-agree")).isDisplayed())
            driver.findElement(By.id("cookie-agree")).click();
        payForm = driver.findElement(By.id("pay-section"));
    }

    @AfterAll
    public static void сloseAll() {
        driver.quit();
    }

    @Test
    void shouldHaveHeader() {
        assertEquals("Онлайн пополнение\nбез комиссии",
                payForm.findElement(By.tagName("h2")).getText());
    }

    @Test
    void shouldHavePayPartnersLogo() {
        WebElement payPartners = payForm.findElement(By.className("pay__partners"));
        List<WebElement> partnersList = payPartners.findElements(By.tagName("img"));
        assertEquals(5, partnersList.size());
    }

    @SneakyThrows
    @Test
    void checkTheLink() {
        HttpURLConnection connection = (HttpURLConnection)
                new URL(driver.findElement(By.linkText("Подробнее о сервисе")).getAttribute("href")).openConnection();
        connection.connect();
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @SneakyThrows
    @Test
    void checkButton() {
        Thread.sleep(5000);
        payForm.findElement(By.id("connection-phone")).sendKeys("297777777");
        payForm.findElement(By.id("connection-sum")).sendKeys("123");
        payForm.findElement(By.className("button__default")).click();
        Thread.sleep(5000);
        WebElement iframePay = driver.findElement(By.className("bepaid-iframe"));
        driver.switchTo().frame(iframePay);
        String headFrame = driver.findElement(By.className("pay-description__cost")).getText();
        System.out.println(headFrame);
    }
}
