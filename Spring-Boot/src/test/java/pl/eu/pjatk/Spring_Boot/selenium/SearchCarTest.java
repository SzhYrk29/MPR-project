package pl.eu.pjatk.Spring_Boot.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchCarTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testSearchCar() {
        SearchCarPage searchCarPage = new SearchCarPage(driver);

        String testId = "1";

        searchCarPage.open()
                .fillInIdInput(testId);

        DisplayCarPage displayCarPage = searchCarPage.clickSubmitButton();

        assertTrue(displayCarPage.isCanWithIdInTable(testId));
    }
}
