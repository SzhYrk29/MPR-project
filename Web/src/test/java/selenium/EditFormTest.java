package selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class EditFormTest {
    private WebDriver driver;

    @BeforeEach
    public void init() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testEditForm() {
        EditFormPage page = new EditFormPage(driver);

        page.open()
                .fillInIdInput("5")
                .fillInBrandInput("Ford")
                .fillInColorInput("Green")
                .clickSubmitButton();
    }
}
