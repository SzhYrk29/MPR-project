package controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pl.edu.pjatk.WebApplication;
import pl.edu.pjatk.model.Car;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyViewControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testViewAllCars() {
        given()
                .when().get("/view/all")
                .then()
                .statusCode(200)
                .body("carList", not(empty()));
    }

    @Test
    public void testSearchForCar() {
        given()
                .when().get("/searchCar")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Search for car</h1>"));
    }

//    @Test
//    public void testDisplayCar() {
//        given()
//                .when().post("/displayCar?id=1")
//                .then()
//                .statusCode(200)
//                .body(containsString("<h1>Display car's info</h1>"));
//    }

    @Test
    public void testDisplayAddCarForm() {
        given()
                .when().get("/addForm")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Add new car</h1>"));
    }

    @Test
    public void testSubmitAddForm() {
        Car car = new Car("Ford", "Blue");

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("brand", car.getBrand())
                .formParam("color", car.getColor())
                .when().post("/addForm")
                .then()
                .statusCode(302)
                .header("Location", containsString("/view/all"));
    }

    @Test
    public void testDisplayEditForm() {
        given()
                .when().get("/editForm")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Edit existing car</h1>"));
    }

    @Test
    public void testSubmitEditForm() {
        Car car = new Car(2L, "Tesla", "Black");

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("id", car.getId())
                .formParam("brand", car.getBrand())
                .formParam("color", car.getColor())
                .when().post("/editForm")
                .then()
                .statusCode(302)
                .header("Location", containsString("/view/all"));
    }

    @Test
    public void testDisplayDeleteForm() {
        given()
                .when().get("/deleteForm")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Delete existing car</h1>"));
    }

//    @Test
//    public void testSubmitDeleteForm() {
//        Car car = new Car(56L, "Hyundai", "Orange");
//
//        given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParam("id", car.getId())
//                .when().post("/deleteForm")
//                .then()
//                .statusCode(302)
//                .header("Location", containsString("/view/all"));
//    }

    @Test
    public void testSearchBrandPage() {
        given()
            .when().get("/search/brand")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Search for cars by brand</h1>"));
    }

    @Test
    public void testDisplayCarsByBrand() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("brand", "Ford")
                .when().post("/displayCarsByBrand")
                .then()
                .statusCode(200)
                .body(containsString("Ford"))
                .body("carList.size()", greaterThan(0));
    }

    @Test
    public void testSearchColorPage() {
        given()
                .when().get("/search/color")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Search for cars by color</h1>"));
    }

    @Test
    public void testDeleteCarNotFound() {
        given()
                .when().post("/deleteForm?id=999") // Assume ID 999 does not exist
                .then()
                .statusCode(404)
                .body(containsString("Car not found"));
    }

    @Test
    public void testDeleteCarInvalidInput() {
        given()
                .when().post("/deleteForm?id=-1") // Invalid ID
                .then()
                .statusCode(409)
                .body(containsString("Invalid input"));
    }

    @Test
    public void testUpdateCarNotFound() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("id", 999)
                .formParam("brand", "Unknown")
                .formParam("color", "Unknown")
                .when().post("/editForm")
                .then()
                .statusCode(404)
                .body(containsString("Car not found"));
    }

    @Test
    public void testUpdateCarInvalidInput() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("id", -1)
                .formParam("brand", "Tesla")
                .formParam("color", "Red")
                .when().post("/editForm")
                .then()
                .statusCode(409)
                .body(containsString("Invalid input"));
    }

//    @Test
//    public void testDisplayCarsByColor() {
//        given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParam("color", "Black")
//                .when().post("/displayCarsByColor")
//                .then()
//                .statusCode(200)
//                .body(containsString("Black")) // Change expectation to "Black"
//                .body("html()", containsString("Black")); // Validate HTML content
//    }

//    @Test
//    public void testSubmitCarForm() {
//        given()
//                .when().get("/addForm")
//                .then()
//                .statusCode(200)
//                .body(containsString("Add a new car"));
//    }

    @Test
    public void testAddCar() {
        Car car = new Car();
        car.setBrand("Toyota");
        car.setColor("Red");

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("brand", car.getBrand())
                .formParam("color", car.getColor())
                .when().post("/addForm")
                .then()
                .statusCode(302)
                .header("Location", containsString("/view/all"));
    }
}
