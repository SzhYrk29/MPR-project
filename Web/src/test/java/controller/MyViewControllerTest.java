package controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    @Test
    public void testDisplayCar() {
        given()
                .when().post("/displayCar?id=1")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Display car's info</h1>"));
    }

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
        Car car = new Car(1L, "Tesla", "White");

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

    @Test
    public void testSubmitDeleteForm() {
        Car car = new Car(1L, "BMW", "Black");

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("id", car.getId())
                .when().post("/deleteForm")
                .then()
                .statusCode(302)
                .header("Location", containsString("/view/all"));
    }

//    @Test
//    public void testSubmitCarForm() {
//        given()
//                .when().get("/addForm")
//                .then()
//                .statusCode(200)
//                .body(containsString("Add a new car"));
//    }
//
//    @Test
//    public void testAddCar() {
//        Car car = new Car();
//        car.setBrand("Toyota");
//        car.setColor("Red");
//
//        given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParam("brand", car.getBrand())
//                .formParam("color", car.getColor())
//                .when().post("/addForm")
//                .then()
//                .statusCode(201)
//                .header("Location", containsString("/view/all"));
//    }
//
//    @Test
//    public void testSearchByBrand() {
//        given()
//                .when().get("/search/brand")
//                .then()
//                .statusCode(200)
//                .body(containsString("Search by brand"));
//    }
//
//    @Test
//    public void testDisplayCarsByBrand() {
//        given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParam("brand", "Toyota")
//                .when().post("/displayCarsByBrand")
//                .then()
//                .statusCode(200)
//                .body("carList.size()", greaterThan(0));
//    }
//
//    @Test
//    public void testDeleteCar() {
//        Car car = new Car();
//        car.setId(1L);
//
//        given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParam("id", car.getId())
//                .when().post("/deleteForm")
//                .then()
//                .statusCode(302)
//                .header("Location", containsString("/view/all"));
//    }
}
