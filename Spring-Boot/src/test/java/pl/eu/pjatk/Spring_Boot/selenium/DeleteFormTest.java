package pl.eu.pjatk.Spring_Boot.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeleteFormTest {
    private WebDriver driver;

    @BeforeEach
    public void init() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testDeleteForm() {
        DeleteFormPage deleteFormPage = new DeleteFormPage(driver);

        String tesId = "5";

        deleteFormPage.open()
                .fillInIdInput(tesId);

        ViewAllPage viewAllPage = deleteFormPage.clickSubmitButton();

        assertFalse(viewAllPage.isCanWithIdInTable(tesId));
    }
}
