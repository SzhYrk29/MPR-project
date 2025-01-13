package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
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
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

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

    @Test
    public void shouldReturnCarsByBrand() {
        String responseBody = """
                [
                    {"id": 1, "brand": "Tesla", "color": "White", "identifier": 12345},
                    {"id": 2, "brand": "Tesla", "color": "Black", "identifier": 34567}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("brand/Tesla"))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        List<Car> actualCars = service.getCarByBrand("Tesla");

        List<Car> expectedCars = List.of(
                new Car(1L, "Tesla", "White", 12345),
                new Car(2L, "Tesla", "Black", 34567)
        );

        assertEquals(expectedCars.getFirst().getBrand(), actualCars.getFirst().getBrand());
    }

    @Test
    public void shouldReturnCarsByColor() {
        String responseBody = """
                [
                    {"id": 1, "brand": "Tesla", "color": "Black", "identifier": 12345},
                    {"id": 2, "brand": "BMW", "color": "Black", "identifier": 34567}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("color/Black"))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        List<Car> actualCars = service.getCarByColor("Black");

        List<Car> expectedCars = List.of(
                new Car(1L, "Tesla", "Black", 12345),
                new Car(2L, "BMW", "Black", 34567)
        );

        assertEquals(expectedCars.getFirst().getColor(), actualCars.getFirst().getColor());
    }

    @Test
    public void shouldAddCar() {
        Car carToAdd = new Car("Tesla", "White");

        String requestBody = """
                {
                    "brand": "Tesla", "color": "White"
                }
                """;

        String responseBody = """
                {
                    "id": 1, "brand": "Tesla", "color": "White", "identifier": 12345
                }
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("car"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().json(requestBody))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("car/1"))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        service.addCar(carToAdd);

        Car addedCar = service.getCarById(1L);

        Car expectedCar = new Car(1L, "Tesla", "White", 12345);

        assertEquals(expectedCar.getId(), addedCar.getId());
        assertEquals(expectedCar.getBrand(), addedCar.getBrand());
        assertEquals(expectedCar.getColor(), addedCar.getColor());
        assertEquals(expectedCar.getIdentifier(), addedCar.getIdentifier());
    }

    @Test
    public void shouldUpdateCar() {
        Car updatedCar = new Car(1L, "Tesla", "White", 12345);

        String requestBody = """
                {
                    "brand": "Tesla", "color": "White"
                }
                """;

        String responseBody = """
                {
                    "id": 1, "brand": "Tesla", "color": "White", "identifier": 12345
                }
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("update/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.PUT))
                .andExpect(MockRestRequestMatchers.content().json(requestBody))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("car/1"))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        service.updateCar(updatedCar.getId(), updatedCar);

        Car addedCar = service.getCarById(1L);

        Car expectedCar = new Car(1L, "Tesla", "White", 12345);

        assertEquals(expectedCar.getId(), addedCar.getId());
        assertEquals(expectedCar.getBrand(), addedCar.getBrand());
        assertEquals(expectedCar.getColor(), addedCar.getColor());
        assertEquals(expectedCar.getIdentifier(), addedCar.getIdentifier());
    }

    @Test
    public void shouldDeleteCar() {
        String allCarsResponseBefore = """
        [
            {"id": 1, "brand": "BMW", "color": "Black", "identifier": 12345},
            {"id": 2, "brand": "Tesla", "color": "Black", "identifier": 67890}
        ]
        """;

        String allCarsResponseAfter = """
        [
            {"id": 2, "brand": "Tesla", "color": "Black", "identifier": 67890}
        ]
        """;

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("car/all"))
                .andRespond(MockRestResponseCreators.withSuccess(allCarsResponseBefore, MediaType.APPLICATION_JSON));

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("delete/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withSuccess());

        customizer.getServer()
                .expect(MockRestRequestMatchers
                        .requestTo("car/all"))
                .andRespond(MockRestResponseCreators.withSuccess(allCarsResponseAfter, MediaType.APPLICATION_JSON));

        List<Car> initialCars = service.getCars();
        assertEquals(2, initialCars.size());

        service.deleteCar(1L);

        List<Car> updatedCars = service.getCars();
        assertEquals(1, updatedCars.size());
        assertEquals("Tesla", updatedCars.getFirst().getBrand());
        assertEquals(2L, updatedCars.getFirst().getId());
    }

}
