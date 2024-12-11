package pl.eu.pjatk.Spring_Boot.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class DisplayCarsByColorPage {
    private final WebDriver driver;

    public DisplayCarsByColorPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isCarWithColorInTable(String color) {
        return driver.findElements(By.xpath("//table//tr[td]")).stream()
                .anyMatch(row -> {
                    WebElement colorCell = row.findElement(By.xpath("./td[3]"));
                    return colorCell.getText().equals(color);
                });
    }
}
