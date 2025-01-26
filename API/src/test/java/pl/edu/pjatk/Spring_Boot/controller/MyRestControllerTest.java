package pl.edu.pjatk.Spring_Boot.controller;

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
public class MyRestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void getCarCarsReturnsAllCars() {
        RestAssured.get(basePath + "/car/all")
                .then()
                .statusCode(200)
                .body("$.size()", is(99));
    }

//    @Test
//    public void postCarCreatesCar() {
//        RestAssured.with()
//                .body(new Car("Ford", "Midnight Blue"))
//                .header("Content-Type", "application/json")
//                .post(basePath + "/car")
//                .then()
//                .statusCode(201);
//    }

    @Test
    public void getCarByIdCarReturnsCarById() {
        RestAssured.get(basePath + "/car/100")
                .then()
                .statusCode(200)
                .body("id", is(100));
    }

    @Test
    public void getCarByIdThrowsCarNotFoundException() {
        RestAssured.get(basePath + "/car/-1")
                .then()
                .statusCode(404);
    }

    @Test
    public void getCarByIdThrowsCarInvalidInputException() {
        RestAssured.get(basePath + "/car/abc")
                .then()
                .statusCode(400);
    }

    @Test
    public void getCarByIdCarReturnsCarByBrand() {
        RestAssured.get(basePath + "/brand/Tesla")
                .then()
                .statusCode(200)
                .body("[0].brand", is("Tesla"));
    }

    @Test
    public void getCarByIdCarReturnsCarByColor() {
        RestAssured.get(basePath + "/color/White")
                .then()
                .statusCode(200)
                .body("[0].color", is("White"));
    }

    @Test
    public void putCarUpdatesCarsFields() {
        RestAssured.with()
                .body(new Car("Tesla", "Midnight Blue"))
                .header("Content-Type", "application/json")
                .put(basePath + "/update/99")
                .then()
                .statusCode(201);
    }

    @Test
    public void putCarThrowsCarNotFoundException() {
        RestAssured.with()
                .body(new Car("Tesla", "Midnight Blue"))
                .header("Content-Type", "application/json")
                .put(basePath + "/update/-3")
                .then()
                .statusCode(404);
    }

    @Test
    public void putCarThrowsInvalidInputException() {
        RestAssured.with()
                .body(new Car("Tesla", "Midnight Blue"))
                .header("Content-Type", "application/json")
                .put(basePath + "/update/fgh")
                .then()
                .statusCode(400);
    }

    @Test
    public void deleteCarDeletesCar() {
        RestAssured.delete(basePath + "/delete/100")
                .then()
                .statusCode(201);
    }

    @Test
    public void deleteCarThrowsCarNotFoundException() {
        RestAssured.delete(basePath + "/delete/-1")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteCarThrowsInvalidInputException() {
        RestAssured.delete(basePath + "/delete/bcd")
                .then()
                .statusCode(400);
    }
}
