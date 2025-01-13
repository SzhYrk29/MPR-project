package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ViewAllPage {
    private final WebDriver driver;

    @FindBy(tagName = "h1")
    private WebElement header;

    public ViewAllPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

//    public ViewAllPage open() {
//        this.driver.get("http://localhost:8080/view/all");
//        return this;
//    }

//    public boolean isHeaderVisible() {
//        return this.header.isDisplayed();
//    }

    public boolean isCarInTable(String brand, String color) {
        return driver.findElements(By.xpath("//table//tr[td]")).stream()
                .anyMatch(row -> {
                    WebElement brandCell = row.findElement(By.xpath("./td[2]"));
                    WebElement colorCell = row.findElement(By.xpath("./td[3]"));
                    return brandCell.getText().equals(brand) && colorCell.getText().equals(color);
                });
    }
}
