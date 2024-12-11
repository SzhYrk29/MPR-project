package pl.eu.pjatk.Spring_Boot.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchColorPage {
    private final WebDriver driver;

    @FindBy(id="color")
    private WebElement colorInput;

    @FindBy(id="submit")
    private WebElement submitButton;

    public SearchColorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public SearchColorPage open() {
        this.driver.get("http://localhost:8080/search/color");
        return this;
    }

    public SearchColorPage fillInColorInput(String text) {
        this.colorInput.sendKeys(text);
        return this;
    }

    public DisplayCarsByColorPage clickSubmitButton() {
        this.submitButton.click();
        return new DisplayCarsByColorPage(this.driver);
    }
}
