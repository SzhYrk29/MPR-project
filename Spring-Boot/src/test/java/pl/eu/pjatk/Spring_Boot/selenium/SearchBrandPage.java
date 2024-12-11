package pl.eu.pjatk.Spring_Boot.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchBrandPage {
    private final WebDriver driver;

    @FindBy(id="brand")
    private WebElement brandInput;

    @FindBy(id="submit")
    private WebElement submitButton;

    public SearchBrandPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public SearchBrandPage open() {
        this.driver.get("http://localhost:8080/search/brand");
        return this;
    }

    public SearchBrandPage fillInBrandInput(String text) {
        this.brandInput.sendKeys(text);
        return this;
    }

    public DisplayCarsByBrandPage clickSubmitButton() {
        this.submitButton.click();
        return new DisplayCarsByBrandPage(this.driver);
    }
}
