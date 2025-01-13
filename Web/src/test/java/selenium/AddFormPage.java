package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddFormPage {
    private final WebDriver driver;

    @FindBy(id="brand")
    private WebElement brandInput;

    @FindBy(id="color")
    private WebElement colorInput;

    @FindBy(id="submit")
    private WebElement submitButton;

    public AddFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public AddFormPage open() {
        this.driver.get("http://localhost:8080/addForm");
        return this;
    }

    public AddFormPage fillInBrandInput(String text) {
        this.brandInput.sendKeys(text);
        return this;
    }

    public AddFormPage fillInColorInput(String text) {
        this.colorInput.sendKeys(text);
        return this;
    }

    public ViewAllPage clickSubmitButton() {
        this.submitButton.click();
        return new ViewAllPage(this.driver);
    }

    public boolean isSubmitButtonVisible() {
        return this.submitButton.isDisplayed();
    }
}
