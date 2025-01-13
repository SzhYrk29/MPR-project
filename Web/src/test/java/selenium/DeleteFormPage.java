package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DeleteFormPage {
    private final WebDriver driver;

    @FindBy(id="id")
    private WebElement idInput;

    @FindBy(id="submit")
    private WebElement submitButton;

    public DeleteFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public DeleteFormPage open() {
        this.driver.get("http://localhost:8080/deleteForm");
        return this;
    }

    public DeleteFormPage fillInIdInput(String text) {
        this.idInput.sendKeys(text);
        return this;
    }

    public DeleteFormPage clickSubmitButton() {
        this.submitButton.click();
        return this;
    }
}
