package pl.eu.pjatk.Spring_Boot.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchCarPage {
    private final WebDriver driver;

    @FindBy(id="id")
    private WebElement idInput;

    @FindBy(id="submit")
    private WebElement submitButton;

    public SearchCarPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public SearchCarPage open() {
        this.driver.get("http://localhost:8080/searchCar");
        return this;
    }

    public SearchCarPage fillInIdInput(String text) {
        this.idInput.sendKeys(text);
        return this;
    }

    public DisplayCarPage clickSubmitButton() {
        this.submitButton.click();
        return new DisplayCarPage(this.driver);
    }
}
