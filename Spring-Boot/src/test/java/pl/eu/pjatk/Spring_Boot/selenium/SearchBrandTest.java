package pl.eu.pjatk.Spring_Boot.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchBrandTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testSearchBrand() {
        SearchBrandPage searchBrandPage = new SearchBrandPage(driver);

        String testBrand = "Tesla";

        searchBrandPage.open()
                .fillInBrandInput(testBrand);

        DisplayCarsByBrandPage displayCarsByBrandPage = searchBrandPage.clickSubmitButton();

        assertTrue(displayCarsByBrandPage.isCarWithBrandInTable(testBrand));
    }
}
