package pl.edu.pjatk.Spring_Boot.service.integrationTests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import pl.edu.pjatk.model.Car;

import static io.restassured.RestAssured.basePath;
import static org.hamcrest.Matchers.is;

@Sql(scripts = {"/insert_cars.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestIntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void getCarsReturnsAllCars() {
        RestAssured.get(basePath + "/car/all")
                .then()
                .statusCode(200)
                .body("$.size()", is(10));
    }

    @Test
    public void postCarCreatesCar() {
        RestAssured.with()
                .body(new Car("Ford", "Midnight Blue"))
                .header("Content-Type", "application/json")
                .post(basePath + "/car")
                .then()
                .statusCode(201);
    }
}
