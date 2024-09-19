package mts.pay;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MTSPayTest {
    ChromeOptions options = new ChromeOptions();
    static WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void test() {
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("https://www.mts.by/");
    }

    @AfterAll
    public static void сloseAll() {
        driver.close();
    }

    @Test
    void shouldHaveHeader() {
        WebElement payForm = driver.findElement(By.id("pay-section"));
        String actual = payForm.findElement(By.tagName("h2")).getText().trim();
        assertEquals("Онлайн пополнение\nбез комиссии", actual);
    }

    @Test
    void shouldHavePayPartnersLogo() {
        WebElement payPartners = driver.findElement(By.className("pay__partners"));
        List<WebElement> partnersList = payPartners.findElements(By.tagName("img"));
        assertEquals(5, partnersList.size());
    }

    @Test
    void checkTheLink() {
        WebElement serviceLink = driver.findElement(By.xpath("//*[@id='pay-section']/div/div/div[2]/section/div/a"));
        assertEquals("https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/", serviceLink.getAttribute("href"));
        //   serviceLink.click();

    }
}
