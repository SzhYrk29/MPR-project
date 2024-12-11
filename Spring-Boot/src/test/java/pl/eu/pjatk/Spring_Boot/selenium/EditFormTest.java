package pl.eu.pjatk.Spring_Boot.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditFormTest {
    private WebDriver driver;

    @BeforeEach
    public void init() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testEditForm() {
        EditFormPage editFormPage = new EditFormPage(driver);

        String testId = "5";
        String testBrand = "Ford";
        String testColor = "Green";

        editFormPage.open()
                .fillInIdInput(testId)
                .fillInBrandInput(testBrand)
                .fillInColorInput(testColor);

        ViewAllPage viewAllPage = editFormPage.clickSubmitButton();

        assertTrue(viewAllPage.isCarWithBrandAndColorInTable(testBrand, testColor));
    }
}
