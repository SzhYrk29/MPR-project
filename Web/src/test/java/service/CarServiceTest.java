package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.Config;
import pl.edu.pjatk.model.Car;
import pl.edu.pjatk.service.CarService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestClientTest(CarService.class)
@ContextConfiguration(classes = Config.class)
public class CarServiceTest {
    MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    RestClient.Builder builder = RestClient.builder();

    CarService service;

    @BeforeEach
    public void setUp() {
        customizer.customize(builder);
        service = new CarService(builder.build());
    }

    @Test
    public void shouldReturnAllCars() {
        String responseBody = """
                [
                    {"id": 1, "brand": "Tesla", "color": "White", "identifier": 12345},
                    {"id": 2, "brand": "BMW", "color": "Black", "identifier": 34567}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("car/all"))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        List<Car> actualCars = service.getCars();

        List<Car> expectedCars = List.of(
                new Car(1L, "Tesla", "White", 12345),
                new Car(2L, "BMW", "Black", 34567)
        );

        assertEquals(expectedCars.getFirst().getId(), actualCars.getFirst().getId());
        assertEquals(expectedCars.getFirst().getBrand(), actualCars.getFirst().getBrand());
        assertEquals(expectedCars.getFirst().getColor(), actualCars.getFirst().getColor());

        assertEquals(expectedCars.getLast().getId(), actualCars.getLast().getId());
        assertEquals(expectedCars.getLast().getBrand(), actualCars.getLast().getBrand());
        assertEquals(expectedCars.getLast().getColor(), actualCars.getLast().getColor());
    }

    @Test
    public void shouldReturnCarById() {
        String responseBody = """
                
                {"id": 1, "brand": "Tesla", "color": "White", "identifier": 12345}
                
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("car/1"))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        Car actualCar = service.getCarById(1L);

        Car expectedCar = new Car(1L, "Tesla", "White", 12345);

        assertEquals(expectedCar.getId(), actualCar.getId());
    }
}
