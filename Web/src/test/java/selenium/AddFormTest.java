package selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddFormTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testAddForm() {
        AddFormPage addFormPage = new AddFormPage(driver);

        String testBrand = "Tesla";
        String testColor = "Black";

        addFormPage.open()
                .fillInBrandInput(testBrand)
                .fillInColorInput(testColor);

        ViewAllPage viewAllPage = addFormPage.clickSubmitButton();

        assertTrue(viewAllPage.isCarInTable(testBrand, testColor));
    }
}
