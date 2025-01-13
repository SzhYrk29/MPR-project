package pl.edu.pjatk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.exception.CarNotFoundException;
import pl.edu.pjatk.exception.EmptyInputException;
import pl.edu.pjatk.exception.InvalidInputException;
import pl.edu.pjatk.model.Car;

import java.util.List;

@Service
public class CarService {
    @Autowired
    RestClient restClient;

    public CarService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Car> getCarByBrand (String brand) {
        return restClient.get()
                .uri("brand/" + brand)
                .retrieve()
                .onStatus(status -> status.value() == 404, ((request, response) -> {
                    throw new CarNotFoundException();
                }))
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Car> getCarByColor (String color) {
        return restClient.get()
                .uri("color/" + color)
                .retrieve()
                .onStatus(status -> status.value() == 404, ((request, response) -> {
                    throw new CarNotFoundException();
                }))
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Car> getCars() {
        return restClient.get()
                .uri("car/all")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Car getCar(Long id) {
        if (id <= 0) {
            throw new InvalidInputException();
        }

        return restClient.get()
                .uri("car/" + id)
                .retrieve()
                .onStatus(status -> status.value() == 404, ((request, response) -> {
                    throw new CarNotFoundException();
                }))
                .onStatus(status -> status.value() == 400, (request, response) -> {
                    throw new InvalidInputException();
                })
                .body(new ParameterizedTypeReference<>() {});
    }

    public void addCar(Car car) {
        ResponseEntity<Void> response = restClient.post()
                .uri("car")
                .contentType(MediaType.APPLICATION_JSON)
                .body(car)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteCar(Long id) {
        if (id <= 0) {
            throw new InvalidInputException();
        }

        ResponseEntity<Void> response = restClient.delete()
                .uri("delete/" + id)
                .retrieve()
                .onStatus(status -> status.value() == 404, ((request, response1) -> {
                    throw new CarNotFoundException();
                }))
                .onStatus(status -> status.value() == 400, ((request, response1) -> {
                    throw new InvalidInputException();
                }))
                .toBodilessEntity();
    }

    public void updateCar (Long id, Car car) {
        if (id <= 0) {
            throw new InvalidInputException();
        }

        ResponseEntity<Void> response = restClient.put()
                .uri("update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(car)
                .retrieve()
                .onStatus(status -> status.value() == 404, ((request, response1) -> {
                    throw new CarNotFoundException();
                }))
                .onStatus(status -> status.value() == 400, ((request, response1) -> {
                    throw new EmptyInputException();
                }))
                .toBodilessEntity();
    }
}
