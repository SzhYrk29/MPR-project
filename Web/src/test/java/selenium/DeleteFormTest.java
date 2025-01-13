package selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DeleteFormTest {
    private WebDriver driver;

    @BeforeEach
    public void init() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testDeleteForm() {
        DeleteFormPage page = new DeleteFormPage(driver);

        page.open()
                .fillInIdInput("6")
                .clickSubmitButton();
    }
}
