package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditFormPage {
    private final WebDriver driver;

    @FindBy(id="id")
    private WebElement idInput;

    @FindBy(id="brand")
    private WebElement brandInput;

    @FindBy(id="color")
    private WebElement colorInput;

    @FindBy(id="submit")
    private WebElement submitButton;

    public EditFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public EditFormPage open() {
        this.driver.get("http://localhost:8080/editForm");
        return this;
    }

    public EditFormPage fillInIdInput(String text) {
        this.idInput.sendKeys(text);
        return this;
    }

    public EditFormPage fillInBrandInput(String text) {
        this.brandInput.sendKeys(text);
        return this;
    }

    public EditFormPage fillInColorInput(String text) {
        this.colorInput.sendKeys(text);
        return this;
    }

    public EditFormPage clickSubmitButton() {
        this.submitButton.click();
        return this;
    }
}
