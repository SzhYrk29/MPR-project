package pl.eu.pjatk.Spring_Boot.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class DisplayCarsByBrandPage {
    private final WebDriver driver;

    public DisplayCarsByBrandPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isCarWithBrandInTable(String brand) {
        return driver.findElements(By.xpath("//table//tr[td]")).stream()
                .anyMatch(row -> {
                    WebElement brandCell = row.findElement(By.xpath("./td[2]"));
                    return brandCell.getText().equals(brand);
                });
    }
}
