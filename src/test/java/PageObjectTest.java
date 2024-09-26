import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import mts.pay.BePaid;
import mts.pay.PayForm;
import mts.pay.PayForm.PaymentDetails;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static mts.pay.BePaid.PayDiscription;
import static mts.pay.BePaid.CardInfo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageObjectTest {
    ChromeOptions options = new ChromeOptions();
    static WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @SneakyThrows
    @BeforeEach
    public void setup() {
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("https://www.mts.by/");
        Thread.sleep(500);
        if (driver.findElement(By.id("bxdynamic_cookies-agreement-gtm_end")).isDisplayed())
            driver.findElement(By.id("cookie-agree")).click();
    }

    @AfterAll
    public static void сloseAll() {
        driver.quit();
    }

    @SneakyThrows
    @Test
    public void test() {
        PayForm payForm = new PayForm(driver);
        PaymentDetails paymentDetails = payForm.generateValidPayment();
        payForm.communicationServices(paymentDetails);
        Thread.sleep(500);
        WebElement iframePay = driver.findElement(By.className("bepaid-iframe"));
        driver.switchTo().frame(iframePay);
        BePaid bePaid = new BePaid(driver);
        List<WebElement> list = bePaid.getCardsBrands();
        String costInfo = bePaid.getCostInfo();
        PayDiscription discription = bePaid.getPayDiscription();
        CardInfo cardInfo = bePaid.getCardInfo();
        String buttonInfo = bePaid.getButtonInfo();
        assertAll(() -> assertEquals(paymentDetails.getAmount(), costInfo),
                () -> assertEquals("375" + paymentDetails.getCustomer(), discription.getNumber()),
                () -> assertEquals("Услуги связи", discription.getPayFor()),
                () -> assertEquals("Номер карты", cardInfo.getNumber()),
                () -> assertEquals(4, bePaid.getCardsBrands().size()),
                () -> assertEquals("Срок действия", cardInfo.getValidPeriod()),
                () -> assertEquals("CVC", cardInfo.getCvc()),
                () -> assertEquals("Имя держателя (как на карте)", cardInfo.getName()),
                () -> assertEquals(paymentDetails.getAmount(), buttonInfo));
    }
}
