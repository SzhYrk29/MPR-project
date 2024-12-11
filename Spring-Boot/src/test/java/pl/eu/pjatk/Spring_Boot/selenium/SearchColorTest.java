package pl.eu.pjatk.Spring_Boot.selenium;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchColorTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @Test
    public void testSearchColor() {
        SearchColorPage searchColorPage = new SearchColorPage(driver);

        String testColor = "Black";

        searchColorPage.open()
                .fillInColorInput(testColor);

        DisplayCarsByColorPage displayCarsByColorPage = searchColorPage.clickSubmitButton();

        assertTrue(displayCarsByColorPage.isCarWithColorInTable(testColor));
    }
}
