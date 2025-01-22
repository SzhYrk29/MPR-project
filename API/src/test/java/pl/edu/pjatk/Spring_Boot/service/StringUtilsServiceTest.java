package pl.edu.pjatk.Spring_Boot.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pjatk.repository.CarRepository;
import pl.edu.pjatk.service.CarService;
import pl.edu.pjatk.service.StringUtilsService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StringUtilsServiceTest {

    @Autowired
    private StringUtilsService stringUtilsService;

    @Test
    public void toUpperCase() {
        StringUtilsService service = new StringUtilsService();

        String result = service.toUpperCase("hello");
        assertEquals("HELLO", result);
    }

    @Test
    public void toLowerCaseExceptFirstLetter() {
        StringUtilsService service = new StringUtilsService();

        String result = service.toLowerCaseExceptFirstLetter("hELLO");
        assertEquals("Hello", result);
    }

    @Test
    public void toLowerCaseWhenStringIsNull() {
        String input = null;

        String result =  stringUtilsService.toLowerCaseExceptFirstLetter(input);

        assertThat(result).isNull();
    }

    @Test
    public void toLowerCaseWhenStringIsEmpty() {
        String input = "";

        String result =  stringUtilsService.toLowerCaseExceptFirstLetter(input);

        assertThat(result).isEmpty();
    }
}
