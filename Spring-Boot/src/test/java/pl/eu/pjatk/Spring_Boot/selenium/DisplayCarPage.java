package pl.eu.pjatk.Spring_Boot.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class DisplayCarPage {
    private final WebDriver driver;

    public DisplayCarPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isCanWithIdInTable(String id) {
        return driver.findElements(By.xpath("//table//tr[td]")).stream()
                .anyMatch(row -> {
                    WebElement idCell = row.findElement(By.xpath("./td[1]"));
                    return idCell.getText().equals(id);
                });
    }
}
