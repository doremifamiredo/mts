package mts.pay;

import lombok.Value;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.By.tagName;

public class BePaid {
    protected WebDriver driver;
    public BePaid(WebDriver driver){
        this.driver = driver;
    }

    private By cost = By.className("pay-description__cost");
    private By dscription = By.className("pay-description__text");
    private By numberCard = By.className("ng-tns-c46-1");
    private By period = By.className("ng-tns-c46-4");
    private By cvc = By.className("ng-tns-c46-5");
    private By name = By.className("ng-tns-c46-3");
    private By button = tagName("button");
    private By cardsBrands = By.className("cards-brands");
    private By cardsBrandsRandom = By.className("cards-brands_random");

    @Value
    public static class CardInfo {
        String number;
        String validPeriod;
        String cvc;
        String name;
    }

    public CardInfo getCardInfo() {
        String number = driver.findElement(numberCard).getText();
        String validPeriod = driver.findElement(period).getText();
        String сvcCode = driver.findElement(cvc).getText();
        String nameCard = driver.findElement(name).getText();
        return new CardInfo(number, validPeriod, сvcCode, nameCard);
    }

    public String getCostInfo() {
        return cutBYN(driver.findElement(cost).getText());
    }

    public String getButtonInfo() {
        return cutBYN(driver.findElement(button).getText()).substring(9);
    }

    public String cutBYN(String text) {
        var cut = text.indexOf(" BYN");
        return text.substring(0, cut);
    }

    public PayDiscription getPayDiscription() {
        String payDiscription = driver.findElement(dscription).getText();
        var cut = payDiscription.indexOf(" Номер:");
        String payFor = payDiscription.substring(8, cut);
        String number = payDiscription.substring(15 + payFor.length());
        return new PayDiscription(payFor, number);
    }

    @Value
    public static class PayDiscription {
        String payFor;
        String number;
    }

    public List<WebElement> getCardsBrands() {
        List<WebElement> list = driver.findElements(cardsBrands)
                .stream()
                .collect(Collectors.toList());
        list.addAll(driver.findElements(cardsBrandsRandom)
                .stream()
                .collect(Collectors.toList()));
        return list;
    }
}
