package mts.pay;

import lombok.SneakyThrows;
import lombok.Value;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public class PayForm {
    protected WebDriver driver;
    private By paySection = By.id("pay-section");
    private By selectHeader = By.className("select__header");
    private By phoneBy = By.id("connection-phone");
    private By totalRub = By.id("connection-sum");
    private By email = By.id("connection-email");
    private By button = By.xpath("//*[@id=\"pay-connection\"]/button");

    public PayForm(WebDriver driver) {
        this.driver = driver;
    }

    @SneakyThrows
    public void communicationServices(PaymentDetails paymentDetails) {
        driver.findElement(phoneBy).sendKeys(paymentDetails.customer);
        driver.findElement(totalRub).sendKeys(paymentDetails.amount);
        driver.findElement(button).click();
        Thread.sleep(5000);
    }

    public static PaymentDetails generateValidPayment() {
        Random random = new Random();
        String phone = "297";
        for (int i = 0; i < 6; i++) {
            phone += random.nextInt(10);
        }
        return new PaymentDetails(phone, Float.toString(random.nextFloat() * 100).substring(0, 5));
    }

    @Value
    public static class PaymentDetails {
        String customer;
        String amount;
    }
}
